package com.fastcampus.deliveryapp.controller.display

import com.fastcampus.deliveryapp.domain.search.SearchFilter
import com.fastcampus.deliveryapp.service.store.StoreService
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class StoreController(
    private val storeService: StoreService,
) {
    companion object {
        private val logger = KotlinLogging.logger(this::class.java.name)
    }

    @GetMapping("/display/store")
    fun list(@RequestParam categoryId: Long, @RequestParam reviewGradeFilterValue: Int, model : Model) : String{
        logger.info { ">>> 카테고리 상점 목록 조회, categoryId: $categoryId, reviewGradeFilterValue: $reviewGradeFilterValue" }
        val availableReviewGradeValue = SearchFilter.Companion.getAvailableReviewGradeValue(reviewGradeFilterValue)
        val stores = storeService.list(categoryId, availableReviewGradeValue)

        model.addAttribute("categoryId", categoryId)
        model.addAttribute("stores", stores)
        model.addAttribute("reviewGradeFilterValue", availableReviewGradeValue)


        return "display/store/store"
    }

    @GetMapping("/display/store/detail")
    fun detail(@RequestParam storeId: Long, model : Model) : String {
        logger.info { ">>> 상점 상세 조회, storeId: $storeId" }
        val storeDetailPageResponse = storeService.detail(storeId)
        model.addAttribute("store", storeDetailPageResponse)

        storeDetailPageResponse?.let {
            model.addAttribute("menus", it.menus)
        }
        return "/display/store/store-detail"
    }
}