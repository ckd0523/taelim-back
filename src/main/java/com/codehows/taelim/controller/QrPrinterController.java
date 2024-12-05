package com.codehows.taelim.controller;

import com.codehows.taelim.dto.QrPrinterDto;
import com.codehows.taelim.entity.QRPrinter;
import com.codehows.taelim.service.QrPrinterService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/printers")
public class QrPrinterController {

    private final QrPrinterService qrPrinterService;

    // 모든 프린터 조회
    @GetMapping("/AllPrinters")
    public List<QRPrinter> getAllPrinters() {
        return qrPrinterService.getAllPrinters();
    }

    // 프린터 추가
    @PostMapping("/AddPrinter")
    public QRPrinter addPrinter(@RequestBody QrPrinterDto printerDto) {
        // DTO를 엔티티로 변환하여 저장
        QRPrinter printer = new QRPrinter();
        printer.setPrinterName(printerDto.getPrinterName());
        printer.setPrinterIp(printerDto.getPrinterIp());
        printer.setIsSelected(printerDto.getIsSelected());

        return qrPrinterService.addPrinter(printer);
    }

    // 프린터 IP 수정
    @PutMapping("/UpdatePrinter")
    public ResponseEntity<QRPrinter> updatePrinterIp(@RequestBody QrPrinterDto printerDto) {
        try {
            System.out.println("123"+printerDto);
            QRPrinter updatedPrinter = qrPrinterService.updatePrinter(printerDto.getQrPrinterNo(), printerDto.getPrinterIp());
            return ResponseEntity.ok(updatedPrinter);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 선택된 프린터 변경
    @PutMapping("/{printerId}/select")
    public void selectPrinter(@PathVariable Long printerId) {
        qrPrinterService.selectPrinter(printerId);
    }

    //선택된 프린터 삭제
    @DeleteMapping("/{printerId}/delete")
    public void deletePrinter(@PathVariable Long printerId) {
        //System.out.println("프린터 삭제 : " + printerId);
        qrPrinterService.deletePrinter(printerId);
    }
}
