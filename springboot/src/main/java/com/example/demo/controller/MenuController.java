package com.example.demo.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Constants;
import com.example.demo.common.Result;
import com.example.demo.entity.Dict;
import com.example.demo.mapper.DictMapper;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.example.demo.service.IMenuService;
import com.example.demo.entity.Menu;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ljw
 * @since 2022-02-24
 */
@RestController
@RequestMapping("/menu")
public class MenuController {
    
    @Resource
    private IMenuService menuService;

    @Resource
    private DictMapper dictMapper;

        @PostMapping
        public Result save(@RequestBody Menu menu) {
            return Result.success(menuService.saveOrUpdate(menu));
        }

        @DeleteMapping("/{id}")
        public Result delete(@PathVariable Integer id) {
            return Result.success(menuService.removeById(id));
        }
        @DeleteMapping("/del/batch")
        public Result deleteBath(@RequestBody List<Integer> ids) {
            return Result.success(menuService.removeByIds(ids));
        }

        @GetMapping
        public Result findAll(@RequestParam(defaultValue = "") String name) {
            return Result.success(menuService.findMenus(name));
        }

        @GetMapping("/{id}")
        public Result findOne(@PathVariable Integer id) {
            return Result.success(menuService.getById(id));
        }

        @GetMapping("/ids")
        public Result findIds() {
            return Result.success(menuService.list().stream().map(Menu::getId));
        }

        @GetMapping("/page")
        public Result findPage(@RequestParam Integer pageNum,
                               @RequestParam Integer pageSize,
                               @RequestParam String name
                                   ) {
            QueryWrapper<Menu> queryWrapper = new QueryWrapper<>();
            queryWrapper.orderByAsc("sort_num");
            if(!"".equals(name)){
                queryWrapper.like("name",name);
            }
            return Result.success(menuService.page(new Page<>(pageNum, pageSize),queryWrapper));
        }

        @GetMapping("/icons")
        public Result getIcons() {
            QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("type", Constants.DICT_TYPE_ICON);
            return Result.success(dictMapper.selectList(queryWrapper));
        }


}

