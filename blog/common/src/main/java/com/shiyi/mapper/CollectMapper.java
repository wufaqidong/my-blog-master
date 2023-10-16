package com.shiyi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.shiyi.entity.Collect;
import com.shiyi.vo.ApiArticleListVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * </p>
 *
 * @author blue
 * @since 2021-08-18
 */
@Repository
public interface CollectMapper extends BaseMapper<Collect> {

    /**
     * 我的收藏列表
     * @return
     */
    Page<ApiArticleListVO> selectCollectList(Page<ApiArticleListVO> tPage, @Param("userId") String userId);
}
