package com.timecapsule.chatservice.controller;

import com.timecapsule.chatservice.api.request.ChatroomReq;
import com.timecapsule.chatservice.api.response.ChatroomRes;
import com.timecapsule.chatservice.service.ChatroomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api("채팅방 API")
@RequiredArgsConstructor
@RestController("/api/chat")
public class ChatroomController {
    private final ChatroomService chatroomService;

    @ApiOperation(value = "채팅방 생성하기", notes = "채팅방을 등록한다")
    @PostMapping("/room")
    public ResponseEntity<?> createChatroom(@RequestBody ChatroomReq chatroomReq) {
        return new ResponseEntity<>(chatroomService.createChatroom(chatroomReq), HttpStatus.CREATED);
    }

    @ApiOperation(value = "채팅방 리스트 보기", notes = "멤버ID에 해당하는 채팅방 리스트를 가져온다")
    @GetMapping("/room/{memberid}")
    public ResponseEntity<?> viewMyChatroom(@PathVariable("memberid") Integer memberId) {
        try {
            List<ChatroomRes> list = chatroomService.getMyChatroomList(memberId);

            if (list != null && !list.isEmpty()) {
                return new ResponseEntity<>(list, HttpStatus.OK);
            } else {
                return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            return exceptionHandling(e);
        }

    }

    private ResponseEntity<?> exceptionHandling(Exception e) {
        e.printStackTrace();
        return new ResponseEntity<String>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
