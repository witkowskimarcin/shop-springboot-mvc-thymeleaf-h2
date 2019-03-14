package com.example.controller.mvc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.example.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.example.entity.Order;
import com.example.entity.OrderDetail;
import com.example.entity.Product;
import com.example.entity.User;
import com.example.model.Cart;
import com.example.service.PromotedProductService;
import com.example.service.UserService;

@Controller
public class ShopController {

	@Autowired private UserService userService;
	@Autowired private UserRepository userRepository;
	@Autowired private ProductRepository productRepository;
	@Autowired private CategoryRepository categoryRepository;
	@Autowired private OrderRepository orderRepository;
	@Autowired private OrderDetailRepository orderDetailRepository;
	@Autowired private SubcategoryRepository subcategoryRepository;
	@Autowired private PromotedProductRepository promotedProductRepository;
	@Autowired private PromotedProductService promotedProductService;
	
	// --------------------------------------
	// PRODUKTY
	// --------------------------------------

	@RequestMapping(value = { "/products" }, method = RequestMethod.GET)
	public ModelAndView showProducts(HttpSession session, @RequestParam(value = "subCatId", required = true) long subCatId) {
		ModelAndView model = new ModelAndView();
		model.setViewName("products");
		
		// pobranie nazwy uzytkownika
		String username;
		try {
		org.springframework.security.core.userdetails.User currentUser = 
				(org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		username = currentUser.getUsername();
		} catch (ClassCastException e) {
			username = "anonymousUser";
		}
		boolean logged = !(username.equals("anonymousUser"));
		
		model.addObject("logged",logged);
		model.addObject("username",username);

		//Iterable<Product> products = productRepository.findAll();
		
		Iterable<Product> products = productRepository.findAllBySubcategory(subcategoryRepository.findById(subCatId));

		model.addObject("products", products);
		
		System.out.println("Sesja: "+session.getId()+ ", uzytkownik: " + username);
		
		Cart cart = Cart.getCartInSession(session);
		model.addObject("cartQuantity",cart.getQuantity());

		return model;
	}

	// strona z produktem
	@GetMapping("/products/product")
	public ModelAndView showProduct(HttpSession session, @RequestParam(value = "code", required = true) long product_code) {
		ModelAndView model = new ModelAndView();
		model.setViewName("product");
		
		// pobranie nazwy uzytkownika
		String username;
		try {
		org.springframework.security.core.userdetails.User currentUser = 
				(org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		username = currentUser.getUsername();
		} catch (ClassCastException e) {
			username = "anonymousUser";
		}
		boolean logged = !(username.equals("anonymousUser"));
		
		model.addObject("logged",logged);
		model.addObject("username",username);
		
		// wypis ID sesji
		System.out.println("ID sesji:" + session.getId());

		Product product = productRepository.findById(product_code);
		model.addObject(product);
		
		Cart cart = Cart.getCartInSession(session);
		model.addObject("cartQuantity",cart.getQuantity());

		List<Integer> iteration = new ArrayList<Integer>();
		for(int i=1; i<product.getImages().size();++i){
			iteration.add(i);
		}
		model.addObject("iteration",iteration);

		return model;
	}

	// dodawanie produktu do koszyka
	@RequestMapping(value = { "/products/product/add" }, method = RequestMethod.GET)
	public String addProductToCart(HttpSession session, @RequestParam(value = "code", required=true) long product_code) {
		
		Cart cart = Cart.getCartInSession(session);
		
		// dodawanie produktu do koszyka
		if (product_code!=0) {
			Product product = productRepository.findById(product_code);
			cart.addProduct(product);
			System.out.println("Dodano produkt o kodzie " + product_code);
			System.out.println("Produkty w koszyku: ");
			
			cart.getProducts().forEach( (key,value)->System.out.println(key.getName() + ":" + value) );
		}

		return "redirect:/products/product?code=" + product_code;
	}
	
	// --------------------------------------
	// !PRODUKTY
	// --------------------------------------
	
	// --------------------------------------
	// KOSZYK
	// --------------------------------------
	
	@RequestMapping(value = { "/cart" }, method = RequestMethod.GET)
	public ModelAndView showCart(HttpSession session) {
		ModelAndView model = new ModelAndView();
		model.setViewName("cart");
		
		// pobranie nazwy uzytkownika
		String username;
		try {
		org.springframework.security.core.userdetails.User currentUser = 
				(org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		username = currentUser.getUsername();
		} catch (ClassCastException e) {
			username = "anonymousUser";
		}
		boolean logged = !(username.equals("anonymousUser"));
		
		model.addObject("logged",logged);
		model.addObject("username",username);
		
		// wypis ID sesji
		System.out.println("ID sesji:" + session.getId());
		Cart cart = Cart.getCartInSession(session);

		model.addObject("cart",cart.getProducts());
		model.addObject("sum",cart.getSum());
		
		System.out.println("Produkty w koszyku:");
		cart.getProducts().forEach( (key,value)->System.out.println(key.getName() + ":" + value) );
		
		model.addObject("cartQuantity",cart.getQuantity());

		if(cart.getQuantity()==0)
		{
			model.addObject("empty",true);
		}
		else {
			model.addObject("empty",false);
		}

		return model;
	}
	
	@RequestMapping(value = { "/cart/plus" }, method = RequestMethod.GET)
	public String cartPlus(HttpSession session, @RequestParam(value = "code", required=true) long product_code) {
		
		Cart cart = Cart.getCartInSession(session);
		
		// dodawanie produktu do koszyka
		if (product_code !=0) {
			Product product = productRepository.findById(product_code);
			cart.addProduct(product);
			System.out.println("Dodano produkt o kodzie " + product_code);
			System.out.println("Produkty w koszyku: ");
			
			// wypis
			cart.getProducts().forEach( (key,value)->System.out.println(key.getName() + ":" + value) );
		}

		return "redirect:/cart";
	}
	
	@RequestMapping(value = { "/cart/minus" }, method = RequestMethod.GET)
	public String cartMinus(HttpSession session, @RequestParam(value = "code", required=true) long product_code) {
		
		Cart cart = Cart.getCartInSession(session);
		
		// dodawanie produktu do koszyka
		if (product_code!=0) {
			Product product = productRepository.findById(product_code);
			cart.removeProduct(product);
			System.out.println("Dodano produkt o kodzie " + product_code);
			System.out.println("Produkty w koszyku: ");
			
			cart.getProducts().forEach( (key,value)->System.out.println(key.getName() + ":" + value) );
		}
		
		return "redirect:/cart";
	}
	
	@GetMapping("/cart/removeAll")
	public String removeCart(HttpSession session) {
		
		Cart cart = Cart.getCartInSession(session);
		cart.removeCart();
		
		return "redirect:/cart";
	}
	
	// --------------------------------------
	// !KOSZYK
	// --------------------------------------
	
	// --------------------------------------
	// ZAMOWIENIE
	// --------------------------------------
	
	@GetMapping("/order")
	public ModelAndView order(HttpSession session) {
		ModelAndView model = new ModelAndView();
		model.setViewName("order");
		
		// pobranie nazwy uzytkownika
		String username = "";
		String firstname = "";
		String lastname = "";
		String locality = "";
		String street = "";
		String zipCode = "";
		String phone = "";
		long user_id = -1;
		User user = null;
		
		try {
		org.springframework.security.core.userdetails.User currentUser = 
				(org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		username = currentUser.getUsername();
		user = userService.findUserByEmail(username);
		firstname = user.getFirstname();
		lastname = user.getLastname();
		locality = user.getLocality();
		street = user.getStreet();
		zipCode = user.getZipCode();
		phone = user.getPhone();
		user_id = user.getId();
		} catch (ClassCastException e) {
			username = "anonymousUser";
		}
		boolean logged = !(username.equals("anonymousUser"));
		
		model.addObject("logged",logged);
		model.addObject("username",username);
		model.addObject("firstname",firstname);
		model.addObject("lastname",lastname);
		model.addObject("locality",locality);
		model.addObject("street",street);
		model.addObject("zipCode",zipCode);
		model.addObject("phone",phone);
		
		// wypis ID sesji
		System.out.println("ID sesji:" + session.getId());
		Cart cart = Cart.getCartInSession(session);

		model.addObject("cart",cart.getProducts());
		model.addObject("sum",cart.getSum());
		model.addObject("cartQuantity",cart.getQuantity());
		
		//System.out.println("Produkty w koszyku:");
		//cart.getProducts().forEach( (key,value)->System.out.println(key.getName() + ":" + value) );
		
		//model.addObject("cartQuantity",cart.getQuantity());
		model.addObject("savedata",new Boolean(true));
		
		return model;
	}
	
	@PostMapping("/order")
	public ModelAndView orderSummary(HttpSession session, @Valid Order order, @RequestParam(value = "save", required = false) Boolean save) {
		ModelAndView model = new ModelAndView();
		model.setViewName("summary");
		
		// pobranie nazwy uzytkownika
		String username = "";
		String firstname = "";
		String lastname = "";
		String locality = "";
		String street = "";
		String zipCode = "";
		String phone = "";
		long user_id = -1;
		User user = null;
		
		try {
		org.springframework.security.core.userdetails.User currentUser = 
				(org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		username = currentUser.getUsername();
		user = userService.findUserByEmail(username);
		firstname = user.getFirstname();
		lastname = user.getLastname();
		locality = user.getLocality();
		street = user.getStreet();
		zipCode = user.getZipCode();
		phone = user.getPhone();
		user_id = user.getId();
		} catch (ClassCastException e) {
			username = "anonymousUser";
		}
		boolean logged = !(username.equals("anonymousUser"));
		
		model.addObject("logged",logged);
		model.addObject("username",username);
		
		// wypis ID sesji
		System.out.println("ID sesji:" + session.getId());
		Cart cart = (Cart) session.getAttribute("myCart");

		model.addObject("cart",cart.getProducts());
		model.addObject("sum",cart.getSum());
		model.addObject("cartQuantity",cart.getQuantity());
		
		if(cart!=null) {
		
	        Date now = new Date();
	        
	        order.setDate(now);
	        order.setCustomer(userService.findUserByEmail(username));
	        
	        List<OrderDetail> orderDetailList = new ArrayList<>();
	        cart.getProducts().forEach((k,v)->{
	        	OrderDetail orderDetail = new OrderDetail();
	        	orderDetail.setProduct(k);
	            orderDetail.setQuantity(v);
	            orderDetailList.add(orderDetail);
	        });
	        
	        order.setOrderDetails(orderDetailList);
	        
	        session.setAttribute("myOrder", order);
	        
	        model.addObject("hashMap", order.getHashMap());
			model.addObject("username",order.getCustomer().getEmail());
			model.addObject("firstname",order.getFirstname());
			model.addObject("lastname",order.getLastname());
			model.addObject("locality",order.getLocality());
			model.addObject("street",order.getStreet());
			model.addObject("zipCode",order.getZipCode());
			model.addObject("phone",order.getPhone());
			model.addObject("description",order.getDescription());

			String shipment = "";
			switch (order.getShipment()) {
			case 1:
				shipment="Przesyłka kurierska - 20 zł (Płatność z góry)";
				break;
			case 2:
				shipment="Paczkomaty Inpost - 10 zł (Płatność z góry)";
				break;
			case 3:
				shipment="List polecony piorytetowy - 10 zł (Płatność z góry)";
				break;
			case 4:
				shipment="Przesyłka kurierska pobraniowa - 20 zł (Płatność przy odbiorze)";
				break;
			case 5:
				shipment="Paczka pocztowa pobraniowa piorytetowa - 15 zł (Płatność przy odbiorze)";
				break;

			default:
				break;
			}
			
			model.addObject("shipment",shipment);

			if(save==null) model.addObject("savedata",false);
			else model.addObject("savedata",save);

		}
		
		return model;
	}
	
	@GetMapping("/order/accept")
	public ModelAndView makeOrder(HttpSession session, @RequestParam(value = "save") Boolean save) {
		
		ModelAndView model = new ModelAndView();
		model.setViewName("/orderAccept");
		
		// pobranie nazwy uzytkownika
		String username;
		try {
		org.springframework.security.core.userdetails.User currentUser = 
				(org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		username = currentUser.getUsername();
		} catch (ClassCastException e) {
			username = "anonymousUser";
		}
		boolean logged = !(username.equals("anonymousUser"));
		
		model.addObject("logged",logged);
		model.addObject("username",username);
		
		// wypis ID sesji
		System.out.println("ID sesji:" + session.getId());
		Cart cart = Cart.getCartInSession(session);
		
		Order order = (Order) session.getAttribute("myOrder");

		boolean fine = true;
		
		if(order!=null) {
			for(OrderDetail od : order.getOrderDetails()){
				orderDetailRepository.save(od);
				Product p = od.getProduct();
				if(p.getQuantity()>=od.getQuantity()) {
					p.setQuantity(p.getQuantity() - od.getQuantity());
					productRepository.save(p);
				}
				else
				{
					fine = false;
					//return "redirect:/order/orderDenied";
					model.setViewName("/orderDenied");
				}
			}

			if(fine) {

				orderRepository.save(order);

				if (save) {
					User u = userRepository.findByEmail(username);
					u.setFirstname(order.getFirstname());
					u.setLastname(order.getLastname());
					u.setLocality(order.getLocality());
					u.setStreet(order.getStreet());
					u.setPhone(order.getPhone());
					u.setZipCode(order.getZipCode());
					userRepository.save(u);
				}
			}
		}
		
		cart.removeCart();
		
		session.removeAttribute("myOrder");

		cart = Cart.getCartInSession(session);

		model.addObject("cart",cart.getProducts());
		model.addObject("sum",cart.getSum());
		model.addObject("cartQuantity",cart.getQuantity());
		
		return model;
	}
	
	// --------------------------------------
	// !ZAMOWIENIE
	// --------------------------------------
}
