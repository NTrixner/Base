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
import { UserDto } from './userDto';


export interface UserListDto { 
    pageSize?: number;
    pagePos?: number;
    ordering?: string;
    items?: Array<UserDto>;
}

