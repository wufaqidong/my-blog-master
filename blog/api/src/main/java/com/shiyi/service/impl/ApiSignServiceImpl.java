package com.shiyi.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shiyi.common.ResponseResult;
import com.shiyi.entity.Prop;
import com.shiyi.entity.Sign;
import com.shiyi.enums.PropTypeEnum;
import com.shiyi.exception.BusinessException;
import com.shiyi.handle.SystemNoticeHandle;
import com.shiyi.im.MessageConstant;
import com.shiyi.mapper.PropMapper;
import com.shiyi.mapper.SignMapper;
import com.shiyi.service.ApiSignService;
import com.shiyi.utils.DateUtil;
import com.shiyi.utils.IpUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApiSignServiceImpl implements ApiSignService {

    private final SignMapper signMapper;

    private final PropMapper propMapper;



    /**
     * 用户的签到记录
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    @Override
    public ResponseResult getSignRecords(String startTime, String endTime) {
        List<String> list = signMapper.getSignRecordsByUserId(startTime,endTime,StpUtil.getLoginIdAsString());
        return ResponseResult.success(list);
    }

    /**
     * 用户签到
     * @param time 签到时间
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult sign(String time) {
        String userId = StpUtil.getLoginIdAsString();

        Sign sign = signMapper.selctSignByUserIdAndTime(userId,time);
        if (sign != null) {
            throw new BusinessException("当日已签到，无需再次签到！");
        }

        String todayStr = DateUtil.dateTimeToStr(new Date(), DateUtil.YYYY_MM_DD);
        if (!todayStr.equals(time)) {
            Prop prop = propMapper.selectOne(new LambdaQueryWrapper<Prop>().eq(Prop::getType, PropTypeEnum.SIGN_CARD.code));
            Integer count = propMapper.selectPropByUserIdAndPropId(userId,prop.getId());
            if (count == null || count == 0) {
                throw new BusinessException("补签卡不足，请关注获取补签卡途径通知！");
            }
            //扣除补签卡 这里的1表示补签扣除的数量 通常还需要判断持有数量是否大于补签扣除的数量
            propMapper.deductPropByUserIdAndPropId(userId,prop.getId(),1);
        }

        sign = Sign.builder().userId(userId).createTime(DateUtil.strToDateTime(time,DateUtil.YYYY_MM_DD)).build();
        signMapper.insert(sign);

        //发送系统通知
        SystemNoticeHandle.sendNotice(userId,MessageConstant.MESSAGE_SYSTEM_NOTICE,MessageConstant.SYSTEM_MESSAGE_CODE,null,null,"恭喜您签到成功");
        return ResponseResult.success();
    }

    /**
     * 验证用户当日是否签到
     * @return
     */
    @Override
    public ResponseResult validateTodayIsSign() {
        String today = DateUtil.dateTimeToStr(DateUtil.getNowDate(), DateUtil.YYYY_MM_DD);
        Sign sign = signMapper.validateTodayIsSign(today,StpUtil.getLoginIdAsString());
        return ResponseResult.success(sign);
    }
}
