package net.catsnap.domain.member.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "회원(모델) 관련 API", description = "회원(모델)을 관리할 수 있는 API입니다.")
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

}
