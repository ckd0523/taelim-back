package com.codehows.taelim.service;

import com.codehows.taelim.dto.QrPrinterDto;
import com.codehows.taelim.entity.QRPrinter;
import com.codehows.taelim.repository.QrPrinterRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QrPrinterService {
    private final QrPrinterRepository qrPrinterRepository;

    // 모든 프린터 조회
    public List<QRPrinter> getAllPrinters() {
        return qrPrinterRepository.findAll();
    }

    // 프린터 추가
    public QRPrinter addPrinter(QRPrinter printer) {
        long count = qrPrinterRepository.count();
        if (count >= 2) {
            throw new IllegalStateException("프린터는 최대 2대만 설정할 수 있습니다.");
        }
        if (count == 1) {
            printer.setIsSelected(false); // 첫 프린터는 기본 선택되지 않음
        } else {
            printer.setIsSelected(true); // 첫 프린터는 기본 선택됨
        }
        return qrPrinterRepository.save(printer);
    }

    // 프린터 IP 수정
    public QRPrinter updatePrinter(Long printerId, String newIp) {
        QRPrinter printer = qrPrinterRepository.findById(printerId)
                .orElseThrow(() -> new EntityNotFoundException("프린터를 찾을 수 없습니다."));
        printer.setPrinterIp(newIp);
        return qrPrinterRepository.save(printer);
    }

    // 선택된 프린터 변경
    public void selectPrinter(Long printerId) {
        // 모든 프린터의 isSelected 값을 false로 설정
        List<QRPrinter> printers = qrPrinterRepository.findAll();
        for (QRPrinter printer : printers) {
            printer.setIsSelected(false);
        }

        // 선택된 프린터의 isSelected 값을 true로 설정
        QRPrinter selectedPrinter = qrPrinterRepository.findById(printerId)
                .orElseThrow(() -> new EntityNotFoundException("프린터를 찾을 수 없습니다."));
        selectedPrinter.setIsSelected(true);

        // 변경된 프린터들을 저장
        qrPrinterRepository.saveAll(printers); // 모든 프린터 상태 저장
        qrPrinterRepository.save(selectedPrinter); // 선택된 프린터 상태 저장
    }

    // isSelected가 true인 프린터 조회 (없을 수 있음)
    public QRPrinter getSelectedPrinter() {
        Optional<QRPrinter> selectedPrinter = qrPrinterRepository.findByIsSelectedTrue();
        // 프린터가 있으면 반환, 없으면 null 반환
        return selectedPrinter.orElse(null);
    }


}
