package br.com.fatec.donationhaapi.utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import br.com.fatec.donationhaapi.entity.*;
import br.com.fatec.donationhaapi.exception.InternalServerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.fatec.donationhaapi.dto.FileUploadResponse;

@Service
public class FileService {
    private static String uploadPath;

    @Value("${file.upload-path}")
    public void setUploadPath(String s) {
        uploadPath = s;
    }

    @Value("${api.url.server}")
    private String urlServer;

    public FileUploadResponse uploadFile(MultipartFile file) {


        // Get original file name
        String originalFileName = file.getOriginalFilename();

        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        // createa random filename
        String randomFileName = UUID.randomUUID().toString() + fileExtension;

        String targetPath = uploadPath + "/" + randomFileName;

        try {
            if (!Files.exists(Path.of(uploadPath))) {
                Files.createDirectories(Path.of(uploadPath));
            }

            // save file
            Files.copy(file.getInputStream(), Path.of(targetPath));

            // return file upload response
            FileUploadResponse fileUploadResponse = new FileUploadResponse();
            fileUploadResponse.setFilename(randomFileName);
            fileUploadResponse.setDownloadUrl(targetPath);

            return fileUploadResponse;

        } catch (Exception exception) {
            throw new InternalServerException("Erro ao salvar imagem: " + exception.getMessage());

        }
    }

    public CampaignProduct personalizePhotosUrl(CampaignProduct campaignProduct) {
        try {
            Product product = campaignProduct.getProduct();
            Institution institution = campaignProduct.getCampaign().getInstitution();
            if (!product.getImgUrl().startsWith(urlServer)) {
                product.setImgUrl(urlServer + product.getImgUrl());
            }
            if (!institution.getIconUrl().startsWith(urlServer)) {
                institution.setIconUrl(urlServer + institution.getIconUrl());
            }
            if (!institution.getBackgroundUrl().startsWith(urlServer)) {
                institution.setBackgroundUrl(urlServer + institution.getBackgroundUrl());
            }
            return campaignProduct;
        } catch (Exception exception) {
            throw new InternalServerException("Erro ao editar Url das imagens: " + exception.getMessage());
        }
    }

    public Campaign personalizePhotosUrl(Campaign campaign) {
        try {
            Institution institution = campaign.getInstitution();
            if (!institution.getIconUrl().startsWith(urlServer)) {
                institution.setIconUrl(urlServer + institution.getIconUrl());
            }
            if (!institution.getBackgroundUrl().startsWith(urlServer)) {
                institution.setBackgroundUrl(urlServer + institution.getBackgroundUrl());
            }
            return campaign;
        } catch (Exception exception) {
            throw new InternalServerException("Erro ao editar Url das imagens: " + exception.getMessage());
        }
    }

    public Donation personalizePhotosUrl(Donation donation) {
        try {
            String donationImgUrl = donation.getImgUrl();
            Product product = donation.getCampaignProduct().getProduct();
            Institution institution = donation.getCampaignProduct().getCampaign().getInstitution();
            if (donationImgUrl != null && !donationImgUrl.startsWith(urlServer)) {
                donation.setImgUrl(urlServer + donationImgUrl);
            }
            String productImgUrl = product.getImgUrl();
            if (productImgUrl != null && !productImgUrl.startsWith(urlServer)) {
                product.setImgUrl(urlServer + productImgUrl);
            }
            String backgroundUrl = institution.getBackgroundUrl();
            if (backgroundUrl != null && !backgroundUrl.startsWith(urlServer)) {
                institution.setBackgroundUrl(urlServer + backgroundUrl);
            }
            String iconUrl = institution.getIconUrl();
            if (iconUrl != null && !iconUrl.startsWith(urlServer)) {
                institution.setIconUrl(urlServer + iconUrl);
            }
            return donation;
        } catch (Exception exception) {
            throw new InternalServerException("Erro ao editar Url das imagens: " + exception.getMessage());
        }
    }

    public Institution personalizePhotosUrl(Institution institution) {
        try {
            String backgroundUrl = institution.getBackgroundUrl();
            if (backgroundUrl != null && !backgroundUrl.startsWith(urlServer)) {
                institution.setBackgroundUrl(urlServer + backgroundUrl);
            }
            String iconUrl = institution.getIconUrl();
            if (iconUrl != null && !iconUrl.startsWith(urlServer)) {
                institution.setIconUrl(urlServer + iconUrl);
            }
            return institution;
        } catch (Exception exception) {
            throw new InternalServerException("Erro ao editar Url das imagens: " + exception.getMessage());
        }
    }

    public Product personalizePhotosUrl(Product product) {
        try {
            String productImgUrl = product.getImgUrl();
            if (productImgUrl != null && !productImgUrl.startsWith(urlServer)) {
                product.setImgUrl(urlServer + productImgUrl);
            }
            return product;
        } catch (Exception exception) {
            throw new InternalServerException("Erro ao editar Url das imagens: " + exception.getMessage());
        }
    }
}
