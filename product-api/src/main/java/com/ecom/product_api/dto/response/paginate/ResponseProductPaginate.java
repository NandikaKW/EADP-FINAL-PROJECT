package com.ecom.product_api.dto.response.paginate;

import com.ecom.product_api.dto.response.ResponseProductDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseProductPaginate {

    private long count;
    private List<ResponseProductDto> dataList;
}
