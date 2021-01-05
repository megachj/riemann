package com.sunset.spring.web_api.controller;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sunset.spring.web_api.service.MyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Slf4j
@RestController("/api/v1")
@RequiredArgsConstructor
@Tag(name = "멤버 관련 API")
public class MyController {

    private final MyService myService;

    @GetMapping("/members/{id}/name")
    @Operation(summary = "멤버 아이디로 이름 조회")
    public ResponseEntity<String> getMemberName(@Parameter(description = "멤버 아이디") @PathVariable("id") int id) {
        return new ResponseEntity<>("TODO", HttpStatus.OK);
    }

    @PutMapping("/members/{id}/name")
    @Operation(summary = "멤버 이름 변경")
    public ResponseEntity<Void> updateMemberName(@Parameter(description = "멤버 아이디") @PathVariable("id") int id,
                                                 @RequestBody @Valid UpdateMemberNameRequest request) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
    @Data
    @Schema
    public static class UpdateMemberNameRequest {
        @Schema(title = "멤버 아이디", example = "1000")
        @NotNull
        private Integer id;

        @Schema(title = "변경할 이름")
        @NotEmpty
        private String changedName;
    }
}
