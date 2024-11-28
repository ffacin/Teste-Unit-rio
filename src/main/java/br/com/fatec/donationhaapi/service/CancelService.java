package br.com.fatec.donationhaapi.service;

import br.com.fatec.donationhaapi.dto.cancel.CancelDto;
import br.com.fatec.donationhaapi.entity.Cancel;
import br.com.fatec.donationhaapi.entity.Donation;
import br.com.fatec.donationhaapi.enums.StatusDonation;
import br.com.fatec.donationhaapi.exception.BadRequestException;
import br.com.fatec.donationhaapi.exception.InternalServerException;
import br.com.fatec.donationhaapi.exception.NotFoundException;
import br.com.fatec.donationhaapi.repository.CancelRepository;
import br.com.fatec.donationhaapi.repository.DonationRepository;
import br.com.fatec.donationhaapi.utils.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CancelService {

    @Autowired
    private CancelRepository cancelRepository;

    @Autowired
    private DonationRepository donationRepository;
    @Autowired
    private FileService fileService;

    public Cancel cancelDonationRequest(CancelDto cancelDto) {
        try {
            Donation donation = donationRepository.findById(cancelDto.getDonationId())
                    .orElseThrow(() -> new NotFoundException("Doação não encontrada."));
            if (donation.getStatusDonation() == StatusDonation.COMPLETE)
                throw new BadRequestException("Doação já realizada.");
            if (donation.getStatusDonation() == StatusDonation.CANCELED)
                throw new BadRequestException("Doação já cancelada.");

            Cancel cancel = new Cancel();
            cancel.setDonation(donation);
            cancel.setDescription(cancelDto.getDescription());
            cancel.setReason(cancelDto.getReason());

            Cancel savedCancel = cancelRepository.saveAndFlush(cancel);
            if (savedCancel.getCancelId().toString().isEmpty())
                throw new InternalServerException("Erro ao cancelar doação, entre em contato com um Administrador");
            donation.setStatusDonation(StatusDonation.CANCELED);
            savedCancel.setDonation(donationRepository.saveAndFlush(donation));
            fileService.personalizePhotosUrl(savedCancel.getDonation());
            return savedCancel;
        } catch (NotFoundException | BadRequestException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new InternalServerException("Erro ao cancelar doação, entre em contato com um Administrador");
        }
    }
}
