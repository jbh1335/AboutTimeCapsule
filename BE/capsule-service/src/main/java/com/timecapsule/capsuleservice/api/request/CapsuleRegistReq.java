package com.timecapsule.capsuleservice.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.timecapsule.capsuleservice.db.entity.RangeType;
//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.util.List;

//@ApiModel(description = "등록할 캡슐 정보가 포함된 Request")
@Getter
public class CapsuleRegistReq {
//    @ApiModelProperty(value = "사용자 id")
    private List<Integer> memberIdList;
//    @ApiModelProperty(value = "제목")
    private String title;
//    @ApiModelProperty(value = "공개 여부")
    private RangeType rangeType;
//    @ApiModelProperty(value = "그룹 여부")
    @JsonProperty("isGroup")
    private boolean isGroup;
//    @ApiModelProperty(value = "위도")
    private Double latitude;
//    @ApiModelProperty(value = "경도")
    private Double longitude;
//    @ApiModelProperty(value = "주소")
    private String address;
}
