/*****************************************************************
 * Copyright (c) 2017 EcoleTree. All Rights Reserved.
 *
 * Author : Seungwoo Jung
 * Create Date : 2024. 02. 23.
 * File Name : Embed.java
 * DESC : 
 *****************************************************************/
package com.gdsc.projectmiobackend.discord;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Embed {
    private String title;
    private String description;
    private String timestamp;
}