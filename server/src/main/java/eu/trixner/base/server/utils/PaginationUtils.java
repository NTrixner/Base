package eu.trixner.base.server.utils;

import eu.trixner.base.dto.PaginationRequestDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class PaginationUtils {

    private PaginationUtils(){
        //Empty Constructor
    }

    public static PageRequest getPageRequest(PaginationRequestDto dto){
        PageRequest pr;
        Sort s = null;
        if(dto.getOrderField() != null){
            if(PaginationRequestDto.OrderDirectionEnum.ASC.equals((dto.getOrderDirection()))){
                s = Sort.by(dto.getOrderField()).ascending();
            }
            else if(PaginationRequestDto.OrderDirectionEnum.DESC.equals((dto.getOrderDirection()))){
                s = Sort.by(dto.getOrderField()).descending();
            }
        }

        if(s == null){
            pr = PageRequest.of(dto.getPage(), dto.getPageSize());
        }
        else{
            pr = PageRequest.of(dto.getPage(), dto.getPageSize(), s);
        }
        return pr;
    }
}
