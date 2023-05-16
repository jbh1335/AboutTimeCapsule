package com.timecapsule.capsuleservice.controller;

import com.timecapsule.capsuleservice.api.request.*;
import com.timecapsule.capsuleservice.api.response.*;
import com.timecapsule.capsuleservice.service.MemoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/capsule/memory")
public class MemoryController {
    private final MemoryService memoryService;

    @PostMapping("/regist")
    public SuccessRes<Integer> registMemory(
            @RequestPart(value = "multipartFileList", required = false) List<MultipartFile> multipartFileList,
            @RequestPart MemoryRegistReq memoryRegistReq) {
        return memoryService.registMemory(multipartFileList, memoryRegistReq);
    }

    @PostMapping
    public SuccessRes<MemoryRes> getMemory(@RequestBody MemoryReq memoryReq) {
        return memoryService.getMemory(memoryReq);
    }

    @PatchMapping("/delete/{memoryId}")
    public CommonRes deleteMemory(@PathVariable("memoryId") int memoryId) {
        return memoryService.deleteMemory(memoryId);
    }

    @GetMapping("/modify/info/{memoryId}")
    public SuccessRes<ModifyMemoryRes> getModifyMemoryInfo(@PathVariable("memoryId") int memoryId) {
        return memoryService.getModifyMemoryInfo(memoryId);
    }

    @PatchMapping("/modify")
    public CommonRes modifyMemory(@RequestPart(value = "multipartFileList", required = false) List<MultipartFile> multipartFileList,
                                  @RequestPart MemoryModifyReq memoryModifyReq) {
        return memoryService.modifyMemory(multipartFileList, memoryModifyReq);
    }

    @PostMapping("/comment/regist")
    public CommonRes registComment(@RequestBody CommentRegistReq commentRegistReq) {
        return memoryService.registComment(commentRegistReq);
    }

    @GetMapping("/comment/{memoryId}")
    public SuccessRes<List<CommentRes>> getComment(@PathVariable("memoryId") int memoryId) {
        return memoryService.getComment(memoryId);
    }

    @PatchMapping("/openDate")
    public CommonRes setGroupFirstOpenDate(@RequestBody GroupOpenDateReq groupOpenDateReq) {
        return memoryService.setGroupFirstOpenDate(groupOpenDateReq);
    }
}
