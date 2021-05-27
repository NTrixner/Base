package eu.trixner.base.server.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Objects;

public class PaginationUtils
{

    private PaginationUtils()
    {
        //Empty Constructor
    }

    public static PageRequest getPageRequest(Integer page, Integer pageSize, String orderField, String orderDirection)
    {
        if (page == null)
        {
            page = 0;
        }
        if (pageSize == null || pageSize < 1)
        {
            pageSize = Integer.MAX_VALUE;
        }
        Sort s;
        if (orderField != null && orderDirection != null && Objects.equals(orderDirection.toUpperCase(), "ASC"))
        {
            s = Sort.by(orderField).ascending();
        }
        else if (orderField != null && orderDirection != null && Objects.equals(orderDirection.toUpperCase(), "DESC"))
        {
            s = Sort.by(orderField).descending();
        }
        else
        {
            s = Sort.unsorted();
        }
        return PageRequest.of(page, pageSize, s);
    }
}
