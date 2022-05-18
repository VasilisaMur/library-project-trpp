package com.project.elib.controllers;

import com.project.elib.models.Books;
import com.project.elib.models.Type;
import com.project.elib.models.User;
import com.project.elib.repo.BooksRepository;
import com.project.elib.service.BooksService;
import com.project.elib.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
public class MainController {

    @Autowired
    private BooksRepository booksRepository;

    @Autowired
    private BooksService bookService;

    @Autowired
    private TypeService typeService;



    @GetMapping("/")
    public String mainPage(Model model,
                           @RequestParam(name = "genre", required = false) Integer genre) {
        model.addAttribute("title", "Main page");
        model.addAttribute("types", typeService.getAllTypes());
        model.addAttribute("genre", genre);
        Iterable<Books> books = bookService.getAllBooks();

        if (genre == null) {
            model.addAttribute("books", books);
        }
        else{
            model.addAttribute("books", bookService.getAllBooksByGenre(genre));
        }

        return "index";
    }

    @Controller
    public class BooksController {

        @GetMapping("/page/{id}")
        public String bookPage (
                @AuthenticationPrincipal User user,
                @PathVariable(value="id") long id,
                Model model) {
            model.addAttribute("bookDesc", "bookDesc");


            Optional<Books> books = booksRepository.findById(id);
            ArrayList<Books> res = new ArrayList<>();
            books.ifPresent(res::add);
            model.addAttribute("books", res);
            return "book-desc";
        }

        @GetMapping("/add")
        public String bookAddForm(Model model){
            return "bookAdd";
        }

        @PostMapping("/add")
        public String bookSave(@RequestParam String book_name, @RequestParam String book_desc,
                               @RequestParam String cover_link, @RequestParam String is_read,
                               @RequestParam String shop_link, @RequestParam int genre, Model model){

            Books book = new Books(
                    book_name,
                    book_desc,
                    cover_link,
                    Boolean.parseBoolean(is_read),
                    shop_link,
                    genre);

            booksRepository.save(book);

            return "redirect:/";
        }
    }


}
