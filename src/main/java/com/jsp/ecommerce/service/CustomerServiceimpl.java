package com.jsp.ecommerce.service;

import java.util.List;
import java.util.Random;

import com.jsp.ecommerce.entity.*;
import com.jsp.ecommerce.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.jsp.ecommerce.dto.UserDto;
import com.jsp.ecommerce.helper.AES;
import com.jsp.ecommerce.helper.EmailSender;
import com.jsp.ecommerce.helper.Status;

import jakarta.servlet.http.HttpSession;

@Service
public class CustomerServiceimpl implements CustomerService {
	@Autowired
	AdminRepository adminRepository;

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	MerchantRepository merchantRepository;

	@Autowired
	EmailSender emailSender;

	@Autowired
	Customer customer;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	CartRepository cartRepository;

	@Autowired
	OrderItemRepository itemRepository;

	public String register(UserDto userDto, Model model) {
		model.addAttribute("userdto", userDto);
		return "customer-register.html";
	}

	@Override
	public String register(UserDto userDto, BindingResult result, HttpSession session) {
//		if (!userDto.getEmail().toLowerCase().endsWith("@gmail.com")) 
//		    result.rejectValue("email", "error.email", "* Only Gmail addresses are allowed");
//		
		if (!userDto.getPassword().equals(userDto.getConfirmPassword()))
			result.rejectValue("confirmPassword", "error.confirmPassword",
					"* Password and Confirm password not matching");

		if (adminRepository.existsByEmail(userDto.getEmail()) || customerRepository.existsByEmail(userDto.getEmail())
				|| merchantRepository.existsByEmail(userDto.getEmail()))
			result.rejectValue("email", "error.email", "* Email Already Exists");

		if (result.hasErrors()) {
			return "customer-register.html";
		}

		int otp = new Random().nextInt(100000, 1000000);
		System.out.println("otp is" + otp);
		emailSender.sendEmail(userDto, otp);

		session.setAttribute("otp", otp);
		session.setAttribute("userDto", userDto);
		session.setAttribute("pass", "Otp Sent Success");
		return "redirect:/customer/otp";
	}

	@Override
	public String sumbitOtp(int otp, HttpSession session) {
		int generatedOtp = (int) session.getAttribute("otp");

		if (generatedOtp == otp) {
			UserDto dto = (UserDto) session.getAttribute("userDto");
			customer.setName(dto.getName());
			customer.setEmail(dto.getEmail());
			customer.setPassword(AES.encrypt(dto.getPassword()));
			customerRepository.save(customer);
			session.setAttribute("pass", "Account created success");
			session.removeAttribute("otp");
			session.removeAttribute("userDto");
			return "redirect:/";
		} else {
			session.setAttribute("fail", "Otp Missmatch");
			return "redirect:/customer/otp";
		}

	}

	@Override
	public String loadHome(HttpSession session) {
		Customer customer = (Customer) session.getAttribute("customer");
		if (customer != null)
			return "customer-home.html";
		else {
			session.setAttribute("fail", "Invalid Session, First Login to Access");
			return "redirect:/login";
		}
	}

	@Override
	public String viewProducts(HttpSession session, Model model, String sort, String search, String category) {
		Customer customer = (Customer) session.getAttribute("customer");
		if (customer != null) {
			List<Product> products = null;

			if (category == null && search == null && sort == null)
				products = productRepository.findByStatus(Status.APPROVED, Sort.by("name").ascending());

			if (sort != null) {
				if (sort.equals("newest"))
					products = productRepository.findByStatus(Status.APPROVED, Sort.by("createdAt").descending());
				else {
					String[] split = sort.split("_");
					if (split[1].equals("asc"))
						products = productRepository.findByStatus(Status.APPROVED, Sort.by(split[0]).ascending());
					else
						products = productRepository.findByStatus(Status.APPROVED, Sort.by(split[0]).descending());
				}
			}
			if (category != null) {
				products = productRepository.findByStatusAndCategory(Status.APPROVED, category,
						Sort.by("name").ascending());
			}
			if (search != null) {
				products = productRepository.findByStatusAndNameLike(Status.APPROVED, "%" + search + "%",
						Sort.by("name").ascending());
			}
			if (products.isEmpty()) {
				session.setAttribute("fail", "No Products Added Yet");
				return "redirect:/customer/home";
			} else {
				model.addAttribute("products", products);
				return "viewApprovedProducts.html";
			}
		} else {
			session.setAttribute("fail", "Invalid Session, First Login to Access");
			return "redirect:/login";
		}

	}

	@Override
	public String addToCart(Long id, HttpSession session) {
		Customer customer = (Customer) session.getAttribute("customer");
		if (customer != null) {
			System.err.println("***** Valid Customer ********");
			Product product = productRepository.findById(id).orElseThrow();
			if (product.getStock() > 0) {
				Cart cart = cartRepository.findByCustomer(customer);
				if (cart == null) {

					System.err.println("***** No Cart new One Created ********");

					cart = new Cart();
					cart.setCustomer(customer);
					cartRepository.save(cart);
				}
				List<OrderItem> items = itemRepository.findByCart(cart);
				if (items.isEmpty()) {
					System.err.println("***** No Items in Cart so directly Added ********");
					OrderItem item = new OrderItem();
					item.setQuantity(1L);
					item.setPrice(product.getPrice());
					item.setProduct(product);
					item.setCart(cart);
					itemRepository.save(item);
					session.setAttribute("pass", product.getName() + " added to cart success");
				} else {
					System.err.println("***** Items are present in Cart ********");
					boolean flag = true;
					for (OrderItem item : items) {
						if (item.getProduct() == product) {
							item.setQuantity(item.getQuantity() + 1);
							itemRepository.save(item);
							session.setAttribute("pass",
									product.getName() + " was already in cart increased quantity success");
							flag = false;
							System.err.println("***** Same Item was there in cart ********");

							break;
						}
					}

					if (flag) {
						System.err.println("***** Items are there but its not the same ********");

						OrderItem item = new OrderItem();
						item.setQuantity(1L);
						item.setPrice(product.getPrice());
						item.setProduct(product);
						item.setCart(cart);
						itemRepository.save(item);
						session.setAttribute("pass", product.getName() + " added to cart success");
					}
				}
			}

			return "redirect:/customer/products";
		} else {
			session.setAttribute("fail", "Invalid Session, First Login to Access");
			return "redirect:/login";
		}
	}

	@Override
	public String viewCart(HttpSession session, Model model) {

		Customer customer = (Customer) session.getAttribute("customer");
		if (customer != null) {
			Cart cart = cartRepository.findByCustomer(customer);
			if (cart == null) {
				session.setAttribute("fail", "No Items in Cart");
				return "redirect:/customer/home";
			} else {
				List<OrderItem> items = itemRepository.findByCart(cart);
				if (items.isEmpty()) {
					session.setAttribute("fail", "No Items in Cart");
					return "redirect:/customer/home";
				} else {
					model.addAttribute("items", items);
					model.addAttribute("total", items.stream().mapToDouble(x -> x.getPrice() * x.getQuantity()).sum());
					return "view-cart.html";
				}
			}
		} else {
			session.setAttribute("fail", "Invalid Session, First Login to Access");
			return "redirect:/login";
		}
	}

	@Override
	public String increaseQuantity(Long id, HttpSession session) {
		Customer customer = (Customer) session.getAttribute("customer");
		if (customer != null) {
			OrderItem item = itemRepository.findById(id).orElseThrow();
			if (item.getQuantity() < item.getProduct().getStock()) {
				item.setQuantity(item.getQuantity() + 1);
				itemRepository.save(item);
				session.setAttribute("pass", "Quantity Increased Success");
				return "redirect:/customer/cart";
			} else {
				session.setAttribute("fail", "Out of Stock");
				return "redirect:/customer/cart";
			}
		} else {
			session.setAttribute("fail", "Invalid Session, First Login to Access");
			return "redirect:/login";
		}
	}

	@Override
	public String decreaseQuantity(Long id, HttpSession session) {
		Customer customer = (Customer) session.getAttribute("customer");
		if (customer != null) {
			OrderItem item = itemRepository.findById(id).orElseThrow();

			if (item.getQuantity() > 1) {
				item.setQuantity(item.getQuantity() - 1);
				itemRepository.save(item);
				session.setAttribute("pass", "Quantity decreased Success");
				return "redirect:/customer/cart";
			} else {
				itemRepository.delete(item);
				session.setAttribute("pass", "Quantity decreased Success");
				return "redirect:/customer/cart";
			}
		} else {
			session.setAttribute("fail", "Invalid Session, First Login to Access");
			return "redirect:/login";
		}
	}
}



