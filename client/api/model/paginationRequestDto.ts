/**
 * Base User API
 * Basic User API for other APIs
 *
 * The version of the OpenAPI document: 1.0
 * Contact: nikolaus@trixner.eu
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


/**
 * A request for Pagination
 */
export interface PaginationRequestDto { 
    page?: number;
    pageSize?: number;
    orderField?: string;
    orderDirection?: PaginationRequestDto.OrderDirectionEnum;
}
export namespace PaginationRequestDto {
    export type OrderDirectionEnum = 'ASC' | 'DESC';
    export const OrderDirectionEnum = {
        ASC: 'ASC' as OrderDirectionEnum,
        DESC: 'DESC' as OrderDirectionEnum
    };
}

