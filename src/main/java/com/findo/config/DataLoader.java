package com.findo.config;

import com.findo.model.Category;
import com.findo.model.City;
import com.findo.model.District;
import com.findo.model.User;
import com.findo.model.enums.UserRole;
import com.findo.model.enums.UserStatus;
import com.findo.repository.CategoryRepository;
import com.findo.repository.CityRepository;
import com.findo.repository.DistrictRepository;
import com.findo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Only load data if tables are empty
        if (categoryRepository.count() == 0) {
            loadCategories();
        }

        if (cityRepository.count() == 0) {
            loadCitiesAndDistricts();
        }

        if (userRepository.count() == 0) {
            loadDefaultUsers();
        }
    }

    private void loadCategories() {
        // Main categories
        Category vehicles = new Category("Araçlar", "Otomobil, motosiklet ve diğer araçlar");
        vehicles.setIcon("car");
        vehicles.setSortOrder(1);
        categoryRepository.save(vehicles);

        Category realEstate = new Category("Emlak", "Satılık ve kiralık konut, işyeri");
        realEstate.setIcon("home");
        realEstate.setSortOrder(2);
        categoryRepository.save(realEstate);

        Category electronics = new Category("Elektronik", "Telefon, bilgisayar, elektronik eşyalar");
        electronics.setIcon("laptop");
        electronics.setSortOrder(3);
        categoryRepository.save(electronics);

        Category homeGarden = new Category("Ev & Bahçe", "Mobilya, beyaz eşya, bahçe malzemeleri");
        homeGarden.setIcon("home-variant");
        homeGarden.setSortOrder(4);
        categoryRepository.save(homeGarden);

        Category fashion = new Category("Moda & Giyim", "Giyim, ayakkabı, aksesuar");
        fashion.setIcon("tshirt-crew");
        fashion.setSortOrder(5);
        categoryRepository.save(fashion);

        // Sub-categories for Vehicles
        Category cars = new Category("Otomobil", "Satılık otomobiller");
        cars.setParent(vehicles);
        cars.setSortOrder(1);
        categoryRepository.save(cars);

        Category motorcycles = new Category("Motosiklet", "Satılık motosikletler");
        motorcycles.setParent(vehicles);
        motorcycles.setSortOrder(2);
        categoryRepository.save(motorcycles);

        // Sub-categories for Electronics
        Category phones = new Category("Cep Telefonu", "Akıllı telefonlar ve cep telefonları");
        phones.setParent(electronics);
        phones.setSortOrder(1);
        categoryRepository.save(phones);

        Category computers = new Category("Bilgisayar", "Masaüstü ve dizüstü bilgisayarlar");
        computers.setParent(electronics);
        computers.setSortOrder(2);
        categoryRepository.save(computers);
    }

    private void loadCitiesAndDistricts() {
        // Major Turkish cities
        City istanbul = new City("İstanbul", "34");
        cityRepository.save(istanbul);

        City ankara = new City("Ankara", "06");
        cityRepository.save(ankara);

        City izmir = new City("İzmir", "35");
        cityRepository.save(izmir);

        City bursa = new City("Bursa", "16");
        cityRepository.save(bursa);

        City antalya = new City("Antalya", "07");
        cityRepository.save(antalya);

        // Districts for Istanbul
        District kadikoy = new District("Kadıköy", istanbul);
        districtRepository.save(kadikoy);

        District besiktas = new District("Beşiktaş", istanbul);
        districtRepository.save(besiktas);

        District sisli = new District("Şişli", istanbul);
        districtRepository.save(sisli);

        District uskudar = new District("Üsküdar", istanbul);
        districtRepository.save(uskudar);

        // Districts for Ankara
        District cankaya = new District("Çankaya", ankara);
        districtRepository.save(cankaya);

        District kecioren = new District("Keçiören", ankara);
        districtRepository.save(kecioren);

        District yenimahalle = new District("Yenimahalle", ankara);
        districtRepository.save(yenimahalle);

        // Districts for Izmir
        District konak = new District("Konak", izmir);
        districtRepository.save(konak);

        District bornova = new District("Bornova", izmir);
        districtRepository.save(bornova);

        District karsiyaka = new District("Karşıyaka", izmir);
        districtRepository.save(karsiyaka);
    }

    private void loadDefaultUsers() {
        // Create admin user
        User admin = new User();
        admin.setFirstName("Admin");
        admin.setLastName("User");
        admin.setEmail("admin@findo.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRole(UserRole.ADMIN);
        admin.setStatus(UserStatus.ACTIVE);
        admin.setEmailVerified(true);
        userRepository.save(admin);

        // Create test user
        User testUser = new User();
        testUser.setFirstName("Test");
        testUser.setLastName("User");
        testUser.setEmail("test@findo.com");
        testUser.setPhone("+905551234567");
        testUser.setPassword(passwordEncoder.encode("test123"));
        testUser.setRole(UserRole.USER);
        testUser.setStatus(UserStatus.ACTIVE);
        testUser.setEmailVerified(true);
        testUser.setPhoneVerified(true);
        userRepository.save(testUser);
    }
}
