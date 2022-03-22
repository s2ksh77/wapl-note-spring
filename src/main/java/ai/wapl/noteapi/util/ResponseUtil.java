package ai.wapl.noteapi.util;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;

public class ResponseUtil {

    public static ResponseEntity<ResponseDTO<?>> success() {
        return ResponseEntity.ok(ResponseDTO.success());
    }

    public static <T> ResponseEntity<ResponseDTO<T>> success(T response) {
        return ResponseEntity.ok(ResponseDTO.success(response));
    }

    public static  ResponseEntity<ResponseDTO<?>> error(HttpHeaders headers, HttpStatus status, String message) {
        return new ResponseEntity<>(ResponseDTO.error(status, message), headers, status);
    }

    public static <T> ResponseEntity<ResponseDTO<T>> notFound() {
        return ResponseEntity.notFound().build();
    }

    public static <T> ResponseEntity<ResponseDTO<T>> noContent() {
        return ResponseEntity.noContent().build();
    }

    @Data
    @NoArgsConstructor
    public static class ResponseDTO<T> implements Serializable {
        private boolean success;

        private T response;

        private Error error;

        ResponseDTO(boolean success, T response, Error error) {
            this.success = success;
            this.response = response;
            this.error = error;
        }

        public static <T> ResponseDTO<T> success() {
            return new ResponseDTO<>(true, null, null);
        }

        public static <T> ResponseDTO<T> success(T response) {
            return new ResponseDTO<>(true, response, null);
        }

        public static <T> ResponseDTO<T> error(HttpStatus status, String message) {
            return new ResponseDTO<>(false, null, new Error(status.value(), message));
        }

        @Data
        static class Error {
            private int status;
            private String message;

            public Error(int status, String message) {
                this.status = status;
                this.message = message;
            }
        }
    }
}
