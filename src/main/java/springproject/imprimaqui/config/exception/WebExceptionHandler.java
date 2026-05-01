package springproject.imprimaqui.config.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;
import springproject.imprimaqui.web.controller.AdminUserController;
import springproject.imprimaqui.web.controller.SiteController;

@ControllerAdvice(assignableTypes = {SiteController.class, AdminUserController.class})
public class WebExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public String handleResponseStatus(ResponseStatusException ex,
                                       Model model,
                                       HttpServletRequest request) {
        model.addAttribute("statusCode", ex.getStatusCode().value());
        model.addAttribute("errorMessage", ex.getReason() != null ? ex.getReason() : "Nao foi possível concluir a solicitação.");
        model.addAttribute("path", request.getRequestURI());
        return "error";
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleValidation(MethodArgumentNotValidException ex,
                                   Model model,
                                   HttpServletRequest request) {
        model.addAttribute("statusCode", HttpStatus.BAD_REQUEST.value());
        model.addAttribute("errorMessage", "Existem campos inválidos na solicitação enviada.");
        model.addAttribute("path", request.getRequestURI());
        return "error";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGeneric(Exception ex,
                                Model model,
                                HttpServletRequest request) {
        model.addAttribute("statusCode", HttpStatus.INTERNAL_SERVER_ERROR.value());
        model.addAttribute("errorMessage", "Ocorreu um erro inesperado ao carregar esta página.");
        model.addAttribute("path", request.getRequestURI());
        return "error";
    }
}
