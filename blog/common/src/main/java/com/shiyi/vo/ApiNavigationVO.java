package com.shiyi.vo;

import com.shiyi.entity.Navigation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiNavigationVO {

    private String siteClassName;

    private List<Navigation> navigations;
}
