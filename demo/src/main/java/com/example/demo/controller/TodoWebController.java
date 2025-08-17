package com.example.demo.controller;

import com.example.demo.entity.Todo;
import com.example.demo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/todos")
public class TodoWebController {

    @Autowired
    private TodoService todoService;

    @GetMapping
    public String listTodos(Model model) {
        model.addAttribute("todos", todoService.getAllTodos());
        model.addAttribute("newTodo", new Todo());
        return "todos/list";
    }

    @PostMapping
    public String createTodo(@ModelAttribute Todo todo, RedirectAttributes redirectAttributes) {
        todoService.createTodo(todo.getTitle(), todo.getDescription());
        redirectAttributes.addFlashAttribute("message", "Todoが作成されました！");
        return "redirect:/todos";
    }

    @PostMapping("/{id}/toggle")
    public String toggleTodo(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            todoService.toggleTodoStatus(id);
            redirectAttributes.addFlashAttribute("message", "Todoの状態が更新されました！");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Todoが見つかりませんでした。");
        }
        return "redirect:/todos";
    }

    @PostMapping("/{id}/delete")
    public String deleteTodo(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        todoService.deleteTodo(id);
        redirectAttributes.addFlashAttribute("message", "Todoが削除されました！");
        return "redirect:/todos";
    }

    @GetMapping("/{id}/edit")
    public String editTodoForm(@PathVariable Long id, Model model) {
        return todoService.getTodoById(id)
                .map(todo -> {
                    model.addAttribute("todo", todo);
                    return "todos/edit";
                })
                .orElse("redirect:/todos");
    }

    @PostMapping("/{id}/edit")
    public String updateTodo(@PathVariable Long id, @ModelAttribute Todo todo, RedirectAttributes redirectAttributes) {
        try {
            todoService.updateTodo(id, todo.getTitle(), todo.getDescription());
            redirectAttributes.addFlashAttribute("message", "Todoが更新されました！");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Todoが見つかりませんでした。");
        }
        return "redirect:/todos";
    }
}