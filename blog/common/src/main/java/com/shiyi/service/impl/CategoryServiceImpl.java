package com.shiyi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shiyi.common.FieldConstants;
import com.shiyi.common.ResponseResult;
import com.shiyi.entity.Article;
import com.shiyi.entity.Category;
import com.shiyi.exception.BusinessException;
import com.shiyi.mapper.ArticleMapper;
import com.shiyi.mapper.CategoryMapper;
import com.shiyi.service.CategoryService;
import com.shiyi.utils.DateUtil;
import com.shiyi.utils.PageUtils;
import com.shiyi.vo.SystemCategoryListVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.shiyi.common.FieldConstants.LIMIT_ONE;
import static com.shiyi.common.ResultCode.CATEGORY_IS_EXIST;
import static com.shiyi.common.ResultCode.CATEGORY_IS_TOP;

/**
 * <p>
 * 博客分类表 服务实现类
 * </p>
 *
 * @author blue
 * @since 2021-12-29
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    private final ArticleMapper articleMapper;

    /**
     * 分类列表
     * @param name 分类名
     * @return
     */
    @Override
    public ResponseResult selectCategoryList(String name) {
        Page<SystemCategoryListVO> categoryPage = baseMapper.selectCategory(new Page<>(PageUtils.getPageNo(), PageUtils.getPageSize()), name);
        return ResponseResult.success(categoryPage);
    }

    /**
     * 分类详情
     * @param id 分类id
     * @return
     */
    @Override
    public ResponseResult getCategoryById(Long id) {
        Category category = baseMapper.selectById(id);
        return ResponseResult.success(category);
    }
    /**
     * 添加分类
     * @param category 分类对象
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult insertCategory(Category category) {
        Category vo = baseMapper.selectOne(new QueryWrapper<Category>().eq(FieldConstants.NAME, category.getName()));
        if (vo != null) {
            throw new BusinessException(CATEGORY_IS_EXIST);
        }
        int rows = baseMapper.insert(category);
        return rows > 0 ? ResponseResult.success(): ResponseResult.error("添加分类失败");
    }

    /**
     * 修改分类
     * @param category 分类对象
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult updateCategory(Category category) {
        Category vo = baseMapper.selectOne(new QueryWrapper<Category>().eq(FieldConstants.NAME, category.getName()));
        if (vo!= null && !vo.getId().equals(category.getId())) {
            throw new BusinessException(CATEGORY_IS_EXIST);
        }

        int rows = baseMapper.updateById(category);
        return rows > 0 ? ResponseResult.success(): ResponseResult.error("修改分类失败");
    }

    /**
     * 删除分类
     * @param id 分类id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteCategory(Long id) {
        Integer count = articleMapper.selectCount(new QueryWrapper<Article>().eq(FieldConstants.CATEGORY_ID, id));
        if (count > 0) {
            throw new BusinessException( "分类下有文章，不能删除");
        }
        int rows = baseMapper.deleteById(id);
        return rows > 0 ? ResponseResult.success(): ResponseResult.error("删除分类失败");
    }

    /**
     * 批量删除分类
     * @param list
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteBatch(List<Category> list) {
        List<Long> ids = new ArrayList<>();
        for (Category category : list) {
            Integer count = articleMapper.selectCount(new QueryWrapper<Article>().eq(FieldConstants.CATEGORY_ID, category.getId()));
            if (count > 0) {
                throw new BusinessException( category.getName() + " 分类下有文章，不能删除");
            }
            ids.add(category.getId());
        }

        int rows = baseMapper.deleteBatchIds(ids);
        return rows > 0 ? ResponseResult.success(): ResponseResult.error("批量删除分类失败");
    }

    /**
     * 置顶分类
     * @return 置顶分类
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult top(Long id) {
        Category category = baseMapper.selectOne(new QueryWrapper<Category>().orderByDesc(FieldConstants.SORT).last(LIMIT_ONE));
        if (category.getId().equals(id)) {
            throw new BusinessException(CATEGORY_IS_TOP);
        }

        Category vo = Category.builder()
                .sort(category.getSort() + 1).updateTime(DateUtil.getNowDate()).id(id).build();
        int rows = baseMapper.updateById(vo);

        return rows > 0? ResponseResult.success(): ResponseResult.error("置顶失败");
    }
}
