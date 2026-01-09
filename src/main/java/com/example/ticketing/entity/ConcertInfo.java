package com.example.ticketing.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
public class ConcertInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // 공연 이름 (예: 아이유 콘서트)

    private int totalStock; // 전체 좌석 수 (100)

    private int currentStock; // 현재 남은 좌석 수 (이게 0 되면 매진!)

    public ConcertInfo(String name, int totalStock) {
        this.name = name;
        this.totalStock = totalStock;
        this.currentStock = totalStock;
    }

    public void decreaseStock(){
        if (this.currentStock <= 0){
            throw new RuntimeException("매진되었습니다!");
        }
        this.currentStock--;
    }
}