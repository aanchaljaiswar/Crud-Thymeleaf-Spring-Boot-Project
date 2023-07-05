package com.crud.thymeleaf.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.crud.thymeleaf.entity.Theme;
import com.crud.thymeleaf.repository.ThemeRepository;

@Controller
public class ThemeController {

	@Autowired
	private ThemeRepository themeRepository;

	@GetMapping("/themes")
	public String getAll(Model model, @Param("keyword") String keyword) {

		try {
			List<Theme> ths = new ArrayList<Theme>();
			if (keyword == null) {
				themeRepository.findAll().forEach(ths::add);
			} else {
				themeRepository.findByTitleContainingIgnoreCase(keyword).forEach(ths::add);
				model.addAttribute("keyword", keyword);
			}

			model.addAttribute("themes", ths);
		} catch (Exception e) {
			model.addAttribute("message", e.getMessage());
		}

		return "themes";

	}

	@GetMapping("/themes/new")
	public String addTheme(Model model) {

		Theme th = new Theme();
		th.setPublished(true);

		model.addAttribute("theme", th);
		model.addAttribute("pageTitle", "Create new Theme");
		return "theme_form";
	}

	@PostMapping("/themes/save")
	public String saveTheme(Theme theme, RedirectAttributes redirectAttributes) {

		try {
			themeRepository.save(theme);

			redirectAttributes.addFlashAttribute("message", "The Theme has been saved successfully!");
		} catch (Exception e) {
			redirectAttributes.addAttribute("message", e.getMessage());
		}
		return "redirect:/themes";
	}

	@GetMapping("/themes/{id}")
	public String editTheme(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {

		try {
			Theme th = themeRepository.findById(id).get();

			model.addAttribute("theme", th);
			model.addAttribute("pageTitle", "Edit Theme [ID: " + id + "]");

			return "theme_form";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/themes";// theme_form
		}
	}

	@GetMapping("/themes/delete/{id}")
	public String deleteTheme(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {

		try {
			themeRepository.deleteById(id);
			redirectAttributes.addFlashAttribute("message","The Theme with id=" + id + " has been deleted successfully!");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/themes";
	}

	@GetMapping("/themes/{id}/published/{status}")
	public String updateThemePublishedStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean published,
			Model model, RedirectAttributes redirectAttributes) {

		try {
			themeRepository.updatePublishedStatus(id, published);

			String status = published ? "published" : "disabled";
			String message = "The Theme id=" + id + " has been " + status;

			redirectAttributes.addFlashAttribute("message", message);
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/themes";
	}
}