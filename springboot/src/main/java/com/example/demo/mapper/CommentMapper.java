package com.example.demo.mapper;

import com.example.demo.entity.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ljw
 * @since 2022-04-18
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

    @Select("select c.*,u.account,u.avatar_url from t_comment c left join user u on c.user_id=u.id where c.report_id=#{reportId}")
    List<Comment> findCommentDetail(@Param("reportId") Integer reportId);
}
