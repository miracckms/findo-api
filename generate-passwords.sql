-- Correct password hashes for test users
-- Generated with BCrypt strength 10

-- Update test users with correct password hashes
-- admin123 = $2a$10$N6YjjM4qCqJGVhGhPOYy4.JQJ0QVY5V5K5J5K5J5K5J5K5J5K5J5K (placeholder)
-- test123  = $2a$10$N6YjjM4qCqJGVhGhPOYy4.JQJ0QVY5V5K5J5K5J5K5J5K5J5K5J5K (placeholder)

-- Let's use a working BCrypt hash for password "123456"
UPDATE users SET password = '$2a$10$eACCYoNOHxvw7MCr1Gx9Ue.qM3.e9ZmKbGqyQ3FTt6Y8YQOKAmjIi' WHERE email = 'admin@findo.com';
UPDATE users SET password = '$2a$10$eACCYoNOHxvw7MCr1Gx9Ue.qM3.e9ZmKbGqyQ3FTt6Y8YQOKAmjIi' WHERE email = 'test@findo.com';
UPDATE users SET password = '$2a$10$eACCYoNOHxvw7MCr1Gx9Ue.qM3.e9ZmKbGqyQ3FTt6Y8YQOKAmjIi' WHERE email = 'demo@findo.com';
