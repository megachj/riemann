package com.sunset.java.basic;

import lombok.Builder;
import lombok.Data;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.Optional;

public class TypeTest {

    @Test
    public void 기본형참조타입_테스트() {
        // 기본형의 참조타입은 null 이 가능하다.
        Boolean isValid = null;
        Assertions.assertThat(isValid).isNull();
    }

    @Test
    public void Optional_테스트() {
        User user1 = User.builder()
                .name("유저1")
                .build();

        Assertions.assertThat(user1.getHasJoined()).isEmpty();
        Throwable thrown = Assertions.catchThrowable(() -> user1.getHasJoined().get());
        Assertions.assertThat(thrown).isInstanceOf(NoSuchElementException.class); // Optional.empty() 일땐 NoSuchElementException 발생

        user1.setHasJoined(Optional.of(Boolean.FALSE));
        Assertions.assertThat(user1.getHasJoined()).isPresent();
        Assertions.assertThat(user1.getHasJoined().get()).isEqualTo(false);

        user1.setHasJoined(Optional.of(Boolean.TRUE));
        Assertions.assertThat(user1.getHasJoined()).isPresent();
        Assertions.assertThat(user1.getHasJoined().get()).isEqualTo(true);

        user1.setHasJoined(Optional.ofNullable(null));
        Assertions.assertThat(user1.getHasJoined()).isEmpty();

        user1.setHasJoined(null);
        Assertions.assertThat(user1.getHasJoined()).isNull(); // 옵셔널도 null 가능
        Assertions.assertThatThrownBy(() -> user1.getHasJoined().get()) // null 일땐 당연히 NullPointerException 발생
                .isInstanceOf(NullPointerException.class);
    }

    @Data
    @Builder
    static class User {
        private String name;
        @Builder.Default // 빌더 기본값
        private Optional<Boolean> hasJoined = Optional.empty();
    }
}
