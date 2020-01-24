package eu.trixner.base.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import java.util.Objects;

/**
 * A request for Pagination
 */
@ApiModel(description = "A request for Pagination")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2020-01-24T14:05:54.448+01:00[Europe/Berlin]")

public class PaginationRequestDto   {
  @JsonProperty("page")
  private Integer page;

  @JsonProperty("pageSize")
  private Integer pageSize;

  @JsonProperty("orderField")
  private String orderField;

  /**
   * Gets or Sets orderDirection
   */
  public enum OrderDirectionEnum {
    ASC("ASC"),
    
    DESC("DESC");

    private Object value;

    OrderDirectionEnum(Object value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static OrderDirectionEnum fromValue(Object value) {
      for (OrderDirectionEnum b : OrderDirectionEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  @JsonProperty("orderDirection")
  private OrderDirectionEnum orderDirection;

  public PaginationRequestDto page(Integer page) {
    this.page = page;
    return this;
  }

  /**
   * Get page
   * @return page
  */
  @ApiModelProperty(value = "")


  public Integer getPage() {
    return page;
  }

  public void setPage(Integer page) {
    this.page = page;
  }

  public PaginationRequestDto pageSize(Integer pageSize) {
    this.pageSize = pageSize;
    return this;
  }

  /**
   * Get pageSize
   * @return pageSize
  */
  @ApiModelProperty(value = "")


  public Integer getPageSize() {
    return pageSize;
  }

  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }

  public PaginationRequestDto orderField(String orderField) {
    this.orderField = orderField;
    return this;
  }

  /**
   * Get orderField
   * @return orderField
  */
  @ApiModelProperty(value = "")


  public String getOrderField() {
    return orderField;
  }

  public void setOrderField(String orderField) {
    this.orderField = orderField;
  }

  public PaginationRequestDto orderDirection(OrderDirectionEnum orderDirection) {
    this.orderDirection = orderDirection;
    return this;
  }

  /**
   * Get orderDirection
   * @return orderDirection
  */
  @ApiModelProperty(value = "")

  @Valid

  public OrderDirectionEnum getOrderDirection() {
    return orderDirection;
  }

  public void setOrderDirection(OrderDirectionEnum orderDirection) {
    this.orderDirection = orderDirection;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PaginationRequestDto paginationRequestDto = (PaginationRequestDto) o;
    return Objects.equals(this.page, paginationRequestDto.page) &&
        Objects.equals(this.pageSize, paginationRequestDto.pageSize) &&
        Objects.equals(this.orderField, paginationRequestDto.orderField) &&
        Objects.equals(this.orderDirection, paginationRequestDto.orderDirection);
  }

  @Override
  public int hashCode() {
    return Objects.hash(page, pageSize, orderField, orderDirection);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PaginationRequestDto {\n");
    
    sb.append("    page: ").append(toIndentedString(page)).append("\n");
    sb.append("    pageSize: ").append(toIndentedString(pageSize)).append("\n");
    sb.append("    orderField: ").append(toIndentedString(orderField)).append("\n");
    sb.append("    orderDirection: ").append(toIndentedString(orderDirection)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

