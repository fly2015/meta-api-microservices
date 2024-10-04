package com.meta.reviewservice.converter;

import com.meta.reviewservice.dto.ReviewDto;
import com.meta.reviewservice.entity.Review;
import com.meta.reviewservice.payload.ReviewRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReviewMapper
{
    ReviewMapper INSTANCE = Mappers.getMapper(ReviewMapper.class);
    ReviewDto toDto(Review review);
    Review toEntity(ReviewDto reviewDto);
    List<ReviewDto> toDto(List<Review> reviews);
    List<Review> toEntity(List<ReviewDto> reviewDtos);
    Review toEntity(ReviewRequest reviewRequest);
}