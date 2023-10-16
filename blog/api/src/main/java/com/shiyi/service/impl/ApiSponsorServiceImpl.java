package com.shiyi.service.impl;

import com.shiyi.mapper.SponsorMapper;
import com.shiyi.service.ApiSponsorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;




/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author blue
 * @since 2021-11-10
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ApiSponsorServiceImpl implements ApiSponsorService {

    private final SponsorMapper sponsorMapper;


}
