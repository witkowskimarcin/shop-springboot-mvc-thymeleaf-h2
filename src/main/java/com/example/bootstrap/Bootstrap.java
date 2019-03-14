package com.example.bootstrap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.example.entity.*;
import com.example.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.parsing.Problem;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import com.example.service.UserService;
import org.springframework.util.StreamUtils;

@Component
public class Bootstrap implements ApplicationListener<ContextRefreshedEvent> {
	
	private final static String sqlCreatePersistanceTable = "CREATE TABLE IF NOT EXISTS  `persistent_logins` (\r\n" + 
			"  `username` varchar(64) NOT NULL,\r\n" + 
			"  `series` varchar(64) NOT NULL,\r\n" + 
			"  `token` varchar(64) NOT NULL,\r\n" + 
			"  `last_used` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,\r\n" + 
			"  PRIMARY KEY  (`series`)\r\n" + 
			") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
	
	private final static String queryAdmin = "INSERT INTO role VALUES(1,'ADMIN')";
	
	private final static String queryUser = "INSERT INTO role VALUES(2,'USER')";

	private final static boolean addData = false;
	
	@Autowired private UserService userService;
	@Autowired private RoleRepository roleRepository;
	@Autowired private ProductRepository productRepository;
	@Autowired private PromotedProductRepository promotedProductRepository;
	@Autowired private CategoryRepository categoryRepository;
	@Autowired private SubcategoryRepository subcategoryRepository;
	@Autowired private ImageRepository imageRepository;
	@Autowired private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	@Autowired public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		
		// dodawanie roli uzytkownikow
		
		Role roleAdmin = new Role();
		roleAdmin.setRole("USER");
		
		Role roleUser = new Role();
		roleUser.setRole("ADMIN");
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		
		if(roleRepository.findByRole("ADMIN")==null) {
			
			namedParameterJdbcTemplate.update(queryAdmin,paramMap);
			namedParameterJdbcTemplate.update(queryUser,paramMap);
			System.out.println("Dodano role do tablicy");
		}
		else {
			System.out.println("Istnieja role");
		}
		
		// dodanie tabeli Persistance
		namedParameterJdbcTemplate.update(sqlCreatePersistanceTable, paramMap);
		
		// dodawanie admina

		User admin = new User();
		admin.setEmail("admin@admin.pl");
		admin.setFirstname("admin");
		admin.setLastname("admin");
		admin.setPassword("qwerty");
		admin.setActive(1);
		
		if(userService.findUserByEmail("admin@admin.pl")==null) {
			userService.saveAdmin(admin);
			System.out.println("Admin nie istnieje, utworzono nowego admina");
		}
		else {
			System.out.println("Admin istnieje");
		}
		
		// dodawanie kategorii

		if(addData) {

			if (categoryRepository.findByName("Komputery") == null) {

				Category c1 = new Category();
				c1.setName("Komputery");
				Category c2 = new Category();
				c2.setName("Laptopy");
				Category c3 = new Category();
				c3.setName("Telefony i tablety");
				Category c4 = new Category();
				c4.setName("RTV");
				Category c5 = new Category();
				c5.setName("Aparaty i kamery");

				categoryRepository.save(c1);
				categoryRepository.save(c2);
				categoryRepository.save(c3);
				categoryRepository.save(c4);
				categoryRepository.save(c5);

				// dodawanie subkategorii

				Subcategory s1 = new Subcategory();
				s1.setName("Procesory");
				s1.setCategory(c1);
				Subcategory s2 = new Subcategory();
				s2.setName("Karty graficzne");
				s2.setCategory(c1);

				Subcategory s5 = new Subcategory();
				s5.setName("Laptopy/Notebooki/Ultrabooki");
				s5.setCategory(c2);
				Subcategory s6 = new Subcategory();
				s6.setName("Laptopy 2 w 1");
				s6.setCategory(c2);
				Subcategory s7 = new Subcategory();
				s7.setName("Akcesoria dla laptopów");
				s7.setCategory(c2);

				Subcategory s3 = new Subcategory();
				s3.setName("Smartfony");
				s3.setCategory(c3);
				Subcategory s4 = new Subcategory();
				s4.setName("Tablety");
				s4.setCategory(c3);

				Subcategory s8 = new Subcategory();
				s8.setName("Telewizory");
				s8.setCategory(c4);
				Subcategory s9 = new Subcategory();
				s9.setName("Kina domowe");
				s9.setCategory(c4);

				Subcategory s10 = new Subcategory();
				s10.setName("Aparaty");
				s10.setCategory(c4);
				Subcategory s11 = new Subcategory();
				s11.setName("Kamery");
				s11.setCategory(c4);

				subcategoryRepository.save(s1);
				subcategoryRepository.save(s2);
				subcategoryRepository.save(s3);
				subcategoryRepository.save(s4);
				subcategoryRepository.save(s5);
				subcategoryRepository.save(s6);
				subcategoryRepository.save(s7);
				subcategoryRepository.save(s8);
				subcategoryRepository.save(s9);
				subcategoryRepository.save(s10);
				subcategoryRepository.save(s11);

				// dodawanie zdjęć

				byte[] f0 = null;
				byte[] f1 = null;
				byte[] f2 = null;
				byte[] f3 = null;
				byte[] f4 = null;
				byte[] f5 = null;
				byte[] f6 = null;
				try {
					ClassPathResource imgFile = new ClassPathResource("static/img/products/1.jpg");
					f0 = StreamUtils.copyToByteArray(imgFile.getInputStream());
					imgFile = new ClassPathResource("static/img/products/6.jpg");
					f1 = StreamUtils.copyToByteArray(imgFile.getInputStream());
					imgFile = new ClassPathResource("static/img/products/2.jpg");
					f2 = StreamUtils.copyToByteArray(imgFile.getInputStream());
					imgFile = new ClassPathResource("static/img/products/3.jpg");
					f3 = StreamUtils.copyToByteArray(imgFile.getInputStream());
					imgFile = new ClassPathResource("static/img/products/4.jpg");
					f4 = StreamUtils.copyToByteArray(imgFile.getInputStream());
					imgFile = new ClassPathResource("static/img/products/5.jpg");
					f5 = StreamUtils.copyToByteArray(imgFile.getInputStream());
					imgFile = new ClassPathResource("static/img/products/6.jpg");
					f6 = StreamUtils.copyToByteArray(imgFile.getInputStream());
				} catch (IOException e) {
					e.printStackTrace();
				}

				Image i0 = new Image();
				i0.setImage(f0);
				Image i1 = new Image();
				i1.setImage(f1);
				Image i2 = new Image();
				i2.setImage(f2);
				Image i3 = new Image();
				i3.setImage(f3);
				Image i4 = new Image();
				i4.setImage(f4);
				Image i5 = new Image();
				i5.setImage(f5);
				Image i6 = new Image();
				i6.setImage(f6);

				imageRepository.save(i0);
				imageRepository.save(i1);
				imageRepository.save(i2);
				imageRepository.save(i3);
				imageRepository.save(i4);
				imageRepository.save(i5);
				imageRepository.save(i6);

				// dodawanie produktow

				Product product1 = new Product();
				product1.setName("Nvidia GTX 1070");
				product1.setDescription("Bardzo wydajna i tania karta graficzna");
				product1.setPrice(1700.99);
				subcategoryRepository.findByName("Procesory");
				product1.setSubcategory(s2);
				product1.setImages(new ArrayList<>());
				product1.getImages().add(i0);
				product1.getImages().add(i1);

				Product product2 = new Product();
				product2.setName("Nvidia GTX 1080 Ti");
				product2.setDescription("Bardzo wydajna karta graficzna");
				product2.setPrice(2800.99);
				product2.setSubcategory(s2);
				product2.setImages(new ArrayList<>());
				product2.getImages().add(i2);

				Product product3 = new Product();
				product3.setName("Intel i7 6600K");
				product3.setDescription("Wydajny procesor");
				product3.setPrice(1590.99);
				product3.setSubcategory(s1);
				product3.setImages(new ArrayList<>());
				product3.getImages().add(i3);

				Product product4 = new Product();
				product4.setName("Intel i5 4690k");
				product4.setDescription("Wydajny i tani procesor");
				product4.setPrice(1300.99);
				product4.setSubcategory(s1);
				product4.setImages(new ArrayList<>());
				product4.getImages().add(i4);

				productRepository.save(product1);
				productRepository.save(product2);
				productRepository.save(product3);
				productRepository.save(product4);

				PromotedProduct pp1 = new PromotedProduct();
				pp1.setProduct(product1);
				PromotedProduct pp2 = new PromotedProduct();
				pp2.setProduct(product2);
				PromotedProduct pp3 = new PromotedProduct();
				pp3.setProduct(product3);

				promotedProductRepository.save(pp1);
				promotedProductRepository.save(pp2);
				promotedProductRepository.save(pp3);

				String queryOpportunityProduct = "INSERT INTO opportunity (id,product_id,promotion_code,quantity) VALUES (1," + product4.getId() + ",'CODE20',20)";
				namedParameterJdbcTemplate.update(queryOpportunityProduct, paramMap);

				System.out.println("Zapisano dane do bazy");
			}
		}
	}
}
