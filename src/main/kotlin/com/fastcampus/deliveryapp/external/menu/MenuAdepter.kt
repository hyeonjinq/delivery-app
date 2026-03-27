package com.fastcampus.deliveryapp.external.menu

import com.fastcampus.deliveryapp.common.http.ExternalHttpApiUtils
import com.fastcampus.deliveryapp.controller.display.dto.MenuDetailDTO
import com.fastcampus.deliveryapp.exception.NotFoundMenuException
import com.fastcampus.deliveryapp.service.menu.MenuService
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange

@Component
class MenuAdepter(
    private val restTemplate: RestTemplate,
) : MenuService {

    @Value("\${apis.delivery-api.host}")
    private lateinit var deliveryApiUrl: String

    @Value("\${apis.delivery-api.menu-detail}")
    private lateinit var menuDetailPath: String

    override fun detail(menuDetailRequest: MenuDetailRequest): MenuDetailDTO {
        val menuDetailFullPath = "$deliveryApiUrl$menuDetailPath/${menuDetailRequest.menuId}?storeId=${menuDetailRequest.storeId}"

        val headers = ExternalHttpApiUtils.getApiHeader(menuDetailRequest.accessToken)
        val httpBody = LinkedMultiValueMap<String, String>()
        httpBody.add("storeId", menuDetailRequest.storeId.toString())

        val request = HttpEntity(httpBody, headers)
        val responseEntity = restTemplate.exchange<MenuDetailResponse>(menuDetailFullPath, HttpMethod.GET, request)

        val menuDetailResponse = responseEntity.body ?: throw NotFoundMenuException("메뉴 정보를 찾을 수 없습니다.")

        return MenuDetailDTO(
            storeId = menuDetailResponse.storeId,
            menuId = menuDetailResponse.menuId,
            menuName = menuDetailResponse.menuName,
            menuPrice = menuDetailResponse.price,
            menuMainImageUrl = menuDetailResponse.menuMainImageUrl,
            menuDescription = menuDetailResponse.description,
        )
    }
}