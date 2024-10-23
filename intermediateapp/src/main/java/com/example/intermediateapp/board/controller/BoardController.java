package com.example.intermediateapp.board.controller;

import com.example.intermediateapp.board.dto.BoardDTO;
import com.example.intermediateapp.board.dto.BoardFileDTO;
import com.example.intermediateapp.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/boards")
public class BoardController {
    private final BoardService boardService;

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody BoardDTO boardDTO) {
        try {
            boardService.save(boardDTO);
            return ResponseEntity.ok().body("글 작성 완료");
        } catch (Exception e) {
            // Log the exception (optional)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("글 작성 실패: " + e.getMessage());
        }
    }

    @GetMapping("/list")
    public ResponseEntity<?> findAll() {
        List<BoardDTO> boardDTOList = boardService.findAll();
        return ResponseEntity.ok().body(boardDTOList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") Long id) {
        // 조회수 처리
        boardService.updateHits(id);
        // 상세내용 가져옴
        BoardDTO boardDTO = boardService.findById(id);
        if (boardDTO.getFileAttached() == 1) {
            List<BoardFileDTO> boardFileDTOList = boardService.findFile(id);
            return ResponseEntity.ok().body(boardFileDTOList);
        }
        return ResponseEntity.ok().body(boardDTO);
    }

    @GetMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id) {
        BoardDTO boardDTO = boardService.findById(id);
        return ResponseEntity.ok().body(boardDTO);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<?> update(BoardDTO boardDTO) {
        boardService.update(boardDTO);
        BoardDTO dto = boardService.findById(boardDTO.getId());
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        boardService.delete(id);
        return ResponseEntity.ok().body("글 삭제 완료");
    }

}
