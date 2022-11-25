package com.practice.mailweb.feature.file;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {
	
	public byte[] resizeImage(MultipartFile file, int newHeight) {
		try {
			BufferedImage bi = ImageIO.read(file.getInputStream());
			
			String extension = FilenameUtils.getExtension(file.getOriginalFilename());	
			int newWidth = getWidthToPerserveScale(newHeight, bi);
			
			BufferedImage resized = resizeImage(bi, newWidth, newHeight);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			if("png".equalsIgnoreCase(extension)) {
				ImageIO.write(paintBackground(resized), extension, baos);
			} else {
				ImageIO.write(resized, extension, baos);
			}
			
			return baos.toByteArray();
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}

	}

	private BufferedImage paintBackground(BufferedImage image) {
		BufferedImage painted = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d =painted.createGraphics();
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, painted.getWidth(), painted.getHeight());
		g2d.drawImage(image, 0, 0, null);
		g2d.dispose();
		return painted;
	}

	private int getWidthToPerserveScale(int newHeight, BufferedImage bi) {
		double oldWidth = bi.getWidth();
		double oldHeight = bi.getHeight();
		int newWidth = (int) (newHeight / oldHeight * oldWidth);
		return newWidth;
	}

	private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight)
			throws IOException {
		Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_DEFAULT);
		BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
		outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
		return outputImage;
	}

}
