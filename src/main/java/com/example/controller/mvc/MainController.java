package com.example.controller.mvc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.example.model.RegisterForm;
import com.example.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.example.entity.Category;
import com.example.entity.Opportunity;
import com.example.entity.Product;
import com.example.entity.Subcategory;
import com.example.entity.User;
import com.example.model.Cart;
import com.example.service.PromotedProductService;
import com.example.service.UserService;

@Controller
public class MainController {

	@Autowired private UserService userService;
	@Autowired private ProductRepository productRepository;
	@Autowired private CategoryRepository categoryRepository;
	@Autowired private SubcategoryRepository subcategoryRepository;
	@Autowired private PromotedProductRepository promotedProductRepository;
	@Autowired private PromotedProductService promotedProductService;
	//@Autowired private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	@Autowired private OpportunityRepository opportunityRepository;

	@RequestMapping(value = {"/"}, method = RequestMethod.GET)
	public ModelAndView index(HttpSession session, HttpServletRequest request) {

		ModelAndView model = new ModelAndView();
		model.setViewName("index");
		
		Iterable<Category> categories = categoryRepository.findAll();
		model.addObject("categories", categories);
		
		Iterable<Subcategory> subcategories = subcategoryRepository.findAll();
		model.addObject("subcategories", subcategories);
		
		ArrayList<Product> promotedProducts = promotedProductService.findAllPromotedProducts();
		model.addObject("promotedProducts", promotedProducts);
		
		ArrayList<Product> promotedProducts1 = new ArrayList<>();
		ArrayList<Product> promotedProducts2 = new ArrayList<>();
		ArrayList<Product> promotedProducts3 = new ArrayList<>();
		
		for(int i = 0; i<3; ++i) {
			if(promotedProducts.size()>i) promotedProducts1.add(promotedProducts.get(i));
			if(promotedProducts.size()>i+3) promotedProducts2.add(promotedProducts.get(i+3));
			if(promotedProducts.size()>i+6) promotedProducts3.add(promotedProducts.get(i+6));
		}
		
		model.addObject("promotedProducts1", promotedProducts1);
		model.addObject("promotedProducts2", promotedProducts2);
		model.addObject("promotedProducts3", promotedProducts3);
		
//		RowMapper<Opportunity> rowMapper = new RowMapper<Opportunity>() {
//
//			@Override
//			public Opportunity mapRow(ResultSet rs, int rowNum) throws SQLException {
//		    	Opportunity o = new Opportunity();
//		        o.setId(rs.getLong("id"));
//		        o.setPromotionCode(rs.getString("promotion_code"));
//		        o.setQuantity(rs.getInt("quantity"));
//		        Product product = new Product();
//		        product.setId(rs.getLong("product_id"));
//		        //Product product = productRepository.findById(rs.getLong("product_id"));
//		        o.setProduct(product);
//		        return o;
//			}
//		};
//
//		String sql = "SELECT * FROM opportunity";
//		Opportunity opportunity = namedParameterJdbcTemplate.queryForObject(sql, new MapSqlParameterSource("id", (int) 1), rowMapper);
//

		if(opportunityRepository.count()>0) {
			Opportunity opportunity = ((ArrayList<Opportunity>) opportunityRepository.findAll()).get(0);
			model.addObject("opportunity", opportunity);
			model.addObject("opportunityExist",true);
		}
		else
		{
			model.addObject("opportunityExist",false);
		}

//		Product productOpportunity = productRepository.findById(opportunity.getProduct().getId());
//		opportunity.setProduct(productOpportunity);
		

		long promotedProductCount = promotedProductRepository.count();
		model.addObject("promotedProductCount",promotedProductCount);
		
		int i = 0;
		model.addObject("i",i);	
		
		// pobranie nazwy uzytkownika 1 sposob
		String username;
		try {
		org.springframework.security.core.userdetails.User currentUser = 
				(org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		username = currentUser.getUsername();
		} catch (ClassCastException e) {
			username = "anonymousUser";
		}
		boolean logged = !(username.equals("anonymousUser"));
		
		System.out.println("flaga: "+ logged + " username: " + username);
		System.out.println("flaga: "+ logged + " username: " + username);
		
		// pobranie nazwy uzytkownika 2 sposob
		try {
			System.out.println(request.getUserPrincipal().getName());
			System.out.println(request.getUserPrincipal().getName());
		} catch (NullPointerException e)
		{
			System.out.println("anonymousUser");
			System.out.println("anonymousUser");
		}
		
		model.addObject("logged",logged);
		model.addObject("username",username);
		
		System.out.println("Sesja: "+session.getId());
		
		Cart cart = Cart.getCartInSession(session);
		model.addObject("cartQuantity",cart.getQuantity());
		
		return model;
	}

	@RequestMapping(value = {"/vue"}, method = RequestMethod.GET)
	public ModelAndView restIndex() {
		ModelAndView model = new ModelAndView();
		model.setViewName("vuejs/index");

		return model;
	}
	
	@RequestMapping(value = {"/login"}, method = RequestMethod.GET)
	public ModelAndView login(HttpSession session) {
		ModelAndView model = new ModelAndView();
		model.setViewName("login");

		Cart cart = Cart.getCartInSession(session);
		model.addObject("cartQuantity",cart.getQuantity());
		
		return model;
	}

	@RequestMapping(value = { "/signup" }, method = RequestMethod.GET)
	public ModelAndView signup(HttpSession session) {
		ModelAndView model = new ModelAndView();
		User user = new User();
		model.addObject("user", user);
		model.setViewName("signup");

        Cart cart = Cart.getCartInSession(session);
        model.addObject("cartQuantity",cart.getQuantity());

		return model;
	}

	@RequestMapping(value = { "/signup" }, method = RequestMethod.POST)
	public ModelAndView createUser(HttpSession session, @Valid RegisterForm form) {
		ModelAndView model = new ModelAndView();
		User user = userService.findUserByEmail(form.getEmail());


		Cart cart = Cart.getCartInSession(session);
		model.addObject("cartQuantity",cart.getQuantity());

		if (user != null) {
			//bindingResult.rejectValue("email", "error.user", "Ten adres e-mail już istnieje!");
			model.addObject("success",false);
            model.addObject("error",true);
            model.addObject("msg", "Taki email już istnieje!");
            model.setViewName("signup");
		}
		else if(!form.getPassword().equals(form.getConfirm_password())){
			model.addObject("success",false);
			model.addObject("error",true);
			model.addObject("msg", "Hasła są różne!");
			model.setViewName("signup");

            //bindingResult.rejectValue("password", "error.user", "Hasła są różne!");
		}
		else if(form.getPassword().length()<6){
			model.addObject("success",false);
			model.addObject("error",true);
			model.addObject("msg", "Hasło jest za krótkie!");
			model.setViewName("signup");

			//bindingResult.rejectValue("password", "error.user", "Hasła są różne!");
		}
		else {
		    user = new User();
		    user.setFirstname(form.getFirstname());
		    user.setLastname(form.getLastname());
		    user.setEmail(form.getEmail());
		    user.setPassword(form.getPassword());
			userService.saveUser(user);
			model.addObject("success",true);
			model.addObject("error",false);
			model.addObject("msg", "Rejestracja przebiegła pomyślnie!");
			model.addObject("user", new User());
			model.setViewName("signup");
		}

		return model;
	}

	@RequestMapping(value = { "/access_denied" }, method = RequestMethod.GET)
	public ModelAndView accessDenied() {
		ModelAndView model = new ModelAndView();
		model.setViewName("access_denied");
		return model;
	}
	
	@GetMapping("/loginorregister")
	public String loginOrRegister(){
		return "login_or_register";
	}
}
