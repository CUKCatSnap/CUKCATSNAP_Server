package com.cuk.catsnap.domain.member.service;

import com.cuk.catsnap.domain.member.dto.MemberRequest;

public interface MemberService {

    void singUp(MemberRequest.MemberSignUp memberSignUp);

}
