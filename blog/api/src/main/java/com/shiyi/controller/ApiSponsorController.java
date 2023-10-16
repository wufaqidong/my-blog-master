package com.shiyi.controller;


import com.shiyi.service.ApiSponsorService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Api(tags = "打赏接口")
@RestController
@RequestMapping("/v1/sponsor")
@RequiredArgsConstructor
public class ApiSponsorController {

    private final ApiSponsorService sponsorService;

}
