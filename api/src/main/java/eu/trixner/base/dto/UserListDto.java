package eu.trixner.base.dto;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import eu.trixner.base.dto.UserDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import org.openapitools.jackson.nullable.JsonNullable;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * UserListDto
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2019-11-22T12:43:10.739+01:00[Europe/Berlin]")

public class UserListDto   {
  @JsonProperty("pageSize")
  private Integer pageSize;

  @JsonProperty("pagePos")
  private Integer pagePos;

  @JsonProperty("ordering")
  private String ordering;

  @JsonProperty("items")
  @Valid
  private List<UserDto> items = null;

  public UserListDto pageSize(Integer pageSize) {
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

  public UserListDto pagePos(Integer pagePos) {
    this.pagePos = pagePos;
    return this;
  }

  /**
   * Get pagePos
   * @return pagePos
  */
  @ApiModelProperty(value = "")


  public Integer getPagePos() {
    return pagePos;
  }

  public void setPagePos(Integer pagePos) {
    this.pagePos = pagePos;
  }

  public UserListDto ordering(String ordering) {
    this.ordering = ordering;
    return this;
  }

  /**
   * Get ordering
   * @return ordering
  */
  @ApiModelProperty(value = "")


  public String getOrdering() {
    return ordering;
  }

  public void setOrdering(String ordering) {
    this.ordering = ordering;
  }

  public UserListDto items(List<UserDto> items) {
    this.items = items;
    return this;
  }

  public UserListDto addItemsItem(UserDto itemsItem) {
    if (this.items == null) {
      this.items = new ArrayList<>();
    }
    this.items.add(itemsItem);
    return this;
  }

  /**
   * Get items
   * @return items
  */
  @ApiModelProperty(value = "")

  @Valid

  public List<UserDto> getItems() {
    return items;
  }

  public void setItems(List<UserDto> items) {
    this.items = items;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserListDto userListDto = (UserListDto) o;
    return Objects.equals(this.pageSize, userListDto.pageSize) &&
        Objects.equals(this.pagePos, userListDto.pagePos) &&
        Objects.equals(this.ordering, userListDto.ordering) &&
        Objects.equals(this.items, userListDto.items);
  }

  @Override
  public int hashCode() {
    return Objects.hash(pageSize, pagePos, ordering, items);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserListDto {\n");
    
    sb.append("    pageSize: ").append(toIndentedString(pageSize)).append("\n");
    sb.append("    pagePos: ").append(toIndentedString(pagePos)).append("\n");
    sb.append("    ordering: ").append(toIndentedString(ordering)).append("\n");
    sb.append("    items: ").append(toIndentedString(items)).append("\n");
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

