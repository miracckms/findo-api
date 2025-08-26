const { spawn } = require("child_process");
const path = require("path");
const fs = require("fs");

// Cache for the Java process
let javaProcess = null;
let isStarting = false;
let startPromise = null;

// Function to find the JAR file
function findJarFile() {
  const targetDir = path.join(process.cwd(), "target");
  if (!fs.existsSync(targetDir)) {
    throw new Error(
      "Target directory not found. Please build the project first."
    );
  }

  const files = fs.readdirSync(targetDir);
  const jarFile = files.find(
    (file) => file.endsWith(".jar") && !file.endsWith(".jar.original")
  );

  if (!jarFile) {
    throw new Error("JAR file not found in target directory.");
  }

  return path.join(targetDir, jarFile);
}

// Function to start the Java application
async function startJavaApp() {
  if (javaProcess && !javaProcess.killed) {
    return javaProcess;
  }

  if (isStarting) {
    return startPromise;
  }

  isStarting = true;
  startPromise = new Promise((resolve, reject) => {
    try {
      const jarPath = findJarFile();
      console.log(`Starting Java application: ${jarPath}`);

      // Set environment variables
      const env = {
        ...process.env,
        SPRING_PROFILES_ACTIVE: "vercel",
        SERVER_PORT: "8080",
        // Override specific properties for serverless
        "SPRING_DATASOURCE_HIKARI_MAXIMUM-POOL-SIZE": "5",
        "SPRING_DATASOURCE_HIKARI_MINIMUM-IDLE": "1",
        "SPRING_DATASOURCE_HIKARI_CONNECTION-TIMEOUT": "10000",
        "SPRING_DATASOURCE_HIKARI_IDLE-TIMEOUT": "300000",
        "SPRING_JPA_HIBERNATE_DDL-AUTO": "none",
        LOGGING_LEVEL_ROOT: "WARN",
        LOGGING_LEVEL_COM_FINDO: "INFO",
      };

      javaProcess = spawn(
        "java",
        [
          "-jar",
          "-Xmx512m",
          "-Xms256m",
          "-Dserver.port=8080",
          "-Dspring.profiles.active=vercel",
          jarPath,
        ],
        {
          env,
          stdio: ["pipe", "pipe", "pipe"],
        }
      );

      let output = "";
      let errorOutput = "";

      javaProcess.stdout.on("data", (data) => {
        const str = data.toString();
        output += str;
        console.log("Java stdout:", str);

        // Check if application has started
        if (
          str.includes("Started FindoApiApplication") ||
          str.includes("Tomcat started on port")
        ) {
          console.log("Java application started successfully");
          isStarting = false;
          resolve(javaProcess);
        }
      });

      javaProcess.stderr.on("data", (data) => {
        const str = data.toString();
        errorOutput += str;
        console.error("Java stderr:", str);
      });

      javaProcess.on("close", (code) => {
        console.log(`Java process exited with code ${code}`);
        javaProcess = null;
        isStarting = false;
        if (code !== 0) {
          reject(
            new Error(
              `Java process exited with code ${code}. Error: ${errorOutput}`
            )
          );
        }
      });

      javaProcess.on("error", (error) => {
        console.error("Failed to start Java process:", error);
        javaProcess = null;
        isStarting = false;
        reject(error);
      });

      // Timeout after 30 seconds
      setTimeout(() => {
        if (isStarting) {
          isStarting = false;
          javaProcess?.kill();
          reject(
            new Error("Java application startup timeout after 30 seconds")
          );
        }
      }, 30000);
    } catch (error) {
      isStarting = false;
      reject(error);
    }
  });

  return startPromise;
}

// Function to make HTTP request to the Java application
async function proxyRequest(req, res) {
  try {
    const { default: fetch } = await import("node-fetch");

    // Ensure Java app is running
    await startJavaApp();

    // Wait a bit for the app to be ready
    await new Promise((resolve) => setTimeout(resolve, 1000));

    const url = `http://localhost:8080${req.url}`;
    console.log(`Proxying request: ${req.method} ${url}`);

    const fetchOptions = {
      method: req.method,
      headers: {
        ...req.headers,
        host: "localhost:8080",
      },
    };

    // Add body for POST/PUT requests
    if (req.method !== "GET" && req.method !== "HEAD") {
      fetchOptions.body = JSON.stringify(req.body);
      fetchOptions.headers["content-type"] = "application/json";
    }

    const response = await fetch(url, fetchOptions);

    // Copy response headers
    response.headers.forEach((value, key) => {
      res.setHeader(key, value);
    });

    res.status(response.status);

    const contentType = response.headers.get("content-type");
    if (contentType && contentType.includes("application/json")) {
      const data = await response.json();
      res.json(data);
    } else {
      const text = await response.text();
      res.send(text);
    }
  } catch (error) {
    console.error("Error proxying request:", error);
    res.status(500).json({
      error: "Internal Server Error",
      message: error.message,
      details: "Failed to proxy request to Java application",
    });
  }
}

// Health check endpoint
async function healthCheck(req, res) {
  try {
    if (!javaProcess || javaProcess.killed) {
      return res.status(503).json({
        status: "unhealthy",
        message: "Java application not running",
      });
    }

    // Try to reach the Java app health endpoint
    const { default: fetch } = await import("node-fetch");
    const response = await fetch("http://localhost:8080/api/actuator/health", {
      timeout: 5000,
    });

    if (response.ok) {
      const data = await response.json();
      res.json(data);
    } else {
      res.status(503).json({
        status: "unhealthy",
        message: "Java application health check failed",
      });
    }
  } catch (error) {
    res.status(503).json({
      status: "unhealthy",
      message: error.message,
    });
  }
}

// Main handler function
module.exports = async (req, res) => {
  console.log(`Received request: ${req.method} ${req.url}`);

  // Enable CORS
  res.setHeader("Access-Control-Allow-Origin", "*");
  res.setHeader(
    "Access-Control-Allow-Methods",
    "GET, POST, PUT, DELETE, OPTIONS"
  );
  res.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");

  if (req.method === "OPTIONS") {
    res.status(200).end();
    return;
  }

  // Health check endpoint
  if (req.url === "/health" || req.url === "/api/health") {
    return healthCheck(req, res);
  }

  // Proxy all other requests to Java application
  return proxyRequest(req, res);
};
