package io.hhplus.tdd.point.lock;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class LockManager {

    private final ConcurrentHashMap<Long, Lock> lockMap = new ConcurrentHashMap<>();

    public <T> T executeOnLock(Long key, Supplier<T> block) throws Exception {
        // 요청 온 userId로 Lock이 없다면 생성, 있다면 가져옴.
        Lock lock = lockMap.computeIfAbsent(key, k -> new ReentrantLock());

        Boolean acquired = false;

        acquired = lock.tryLock(5, TimeUnit.SECONDS);
        if(!acquired)
            throw new RuntimeException("TimeOut!!");

        try {
            return block.get();
        } finally {
            if(acquired)
                lock.unlock();
        }
    }
}
