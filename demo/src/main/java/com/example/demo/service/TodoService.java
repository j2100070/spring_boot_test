package com.example.demo.service;

import com.example.demo.entity.Todo;
import com.example.demo.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;

    public List<Todo> getAllTodos() {
        return todoRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<Todo> getTodosByStatus(boolean completed) {
        return todoRepository.findByCompletedOrderByCreatedAtDesc(completed);
    }

    public Optional<Todo> getTodoById(Long id) {
        return todoRepository.findById(id);
    }

    public Todo saveTodo(Todo todo) {
        return todoRepository.save(todo);
    }

    public Todo createTodo(String title, String description) {
        Todo todo = new Todo(title, description);
        return todoRepository.save(todo);
    }

    public Todo updateTodo(Long id, String title, String description) {
        Optional<Todo> optionalTodo = todoRepository.findById(id);
        if (optionalTodo.isPresent()) {
            Todo todo = optionalTodo.get();
            todo.setTitle(title);
            todo.setDescription(description);
            todo.setUpdatedAt(LocalDateTime.now());
            return todoRepository.save(todo);
        }
        throw new RuntimeException("Todo not found with id: " + id);
    }

    public Todo toggleTodoStatus(Long id) {
        Optional<Todo> optionalTodo = todoRepository.findById(id);
        if (optionalTodo.isPresent()) {
            Todo todo = optionalTodo.get();
            todo.setCompleted(!todo.isCompleted());
            todo.setUpdatedAt(LocalDateTime.now());
            return todoRepository.save(todo);
        }
        throw new RuntimeException("Todo not found with id: " + id);
    }

    public void deleteTodo(Long id) {
        todoRepository.deleteById(id);
    }
}