package com.timecapsule.capsuleservice.service;

import com.timecapsule.capsuleservice.db.entity.Memory;
import com.timecapsule.capsuleservice.db.repository.MemoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SchedulerService {
    private final MemoryRepository memoryRepository;
    @Scheduled(cron = "0 0 0 * * *")
    public void unlockMemory() {
        List<Memory> memoryList = memoryRepository.findAllByIsDeletedFalseAndIsLockedTrue();

        memoryList.forEach(memory -> {
            if(memory.getOpenDate().equals(LocalDate.now())) memoryRepository.save(Memory.unLockMemory(memory, false));
        });
    }
}
