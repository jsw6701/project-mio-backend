/*****************************************************************
 * Copyright (c) 2017 EcoleTree. All Rights Reserved.
 *
 * Author : Seungwoo Jung
 * Create Date : 2024. 02. 23.
 * File Name : Message.java
 * DESC : 
 *****************************************************************/
package com.gdsc.projectmiobackend.discord;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Message {
    private String content;
    private List<Embed> embeds;
}