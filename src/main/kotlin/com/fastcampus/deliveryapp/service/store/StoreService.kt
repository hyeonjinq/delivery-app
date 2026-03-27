package com.fastcampus.deliveryapp.service.store

import com.fastcampus.deliveryapp.domain.store.CategoryStore

interface StoreService {
    fun list(categoryId: Long, reviewGradeFilterValue: Int): List<CategoryStore>
}