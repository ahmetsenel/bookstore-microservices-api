package com.ahmetsenel.bookservice.mapper;

import com.ahmetsenel.bookservice.dto.BookDetail;
import com.ahmetsenel.bookservice.dto.BookRequest;
import com.ahmetsenel.bookservice.dto.BookSummary;
import com.ahmetsenel.bookservice.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Book toEntity(BookRequest bookRequest);

    BookSummary toSummary(Book book);

    BookDetail toDetail(Book book);
}
