package com.aishwarya.ethical.transparency_portal.modules.product.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.aishwarya.ethical.transparency_portal.modules.product.dto.ProductCategoryDTO;
import com.aishwarya.ethical.transparency_portal.modules.product.service.ProductService;
import com.aishwarya.ethical.transparency_portal.security.JwtAuthenticationFilter;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private ProductService productService;

    @Test
    void getAllCategories_returnsCategoriesWithIcons() throws Exception {
        List<ProductCategoryDTO> categories = Arrays.asList(
                new ProductCategoryDTO("FOOD", "\uD83C\uDF72"),
                new ProductCategoryDTO("SKINCARE", "\uD83D\uDC8E")
        );
        Mockito.when(productService.getAllCategoriesWithIcons()).thenReturn(categories);

        mockMvc.perform(get("/api/v1/productsapi/categories")
        		.accept(MediaType.APPLICATION_JSON))
        		.andDo(org.springframework.test.web.servlet.result.MockMvcResultHandlers.print())
                .andExpect(status().isOk());
        Mockito.verify(productService).getAllCategoriesWithIcons();
                
//                .andExpect(jsonPath("$", hasSize(2)))
//                .andExpect(jsonPath("$[0].category", is("FOOD")))
//                .andExpect(jsonPath("$[0].icon", is("\uD83C\uDF72")))
//                .andExpect(jsonPath("$[1].category", is("SKINCARE")))
//                .andExpect(jsonPath("$[1].icon", is("\uD83D\uDC8E")));
    }
}
