package cn.cas.banana.config;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.ResourceResolver;
import org.springframework.web.servlet.resource.ResourceResolverChain;

/** Redirects every page to index.html Used to handle the router */
@Configuration
@Slf4j
public class SinglePageAppConfig implements WebMvcConfigurer {

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry
        .addResourceHandler("/**")
        .addResourceLocations("classpath:/static/")
        .setCacheControl(CacheControl.noCache())
        .resourceChain(false)
        .addResolver(new PushStateResourceResolver());
  }

  private static class PushStateResourceResolver implements ResourceResolver {
    private final Resource index = new ClassPathResource("/static/index.html");
    private final List<String> handledExtensions =
        Arrays.asList(
            "html",
            "js",
            "json",
            "css",
            "png",
            "svg",
            "eot",
            "ttf",
            "woff",
            "appcache",
            "jpg",
            "jpeg",
            "gif",
            "ico",
            "txt",
            "map");
    private final List<String> ignoredPaths = Arrays.asList("api", "error", "actuator");

    @Override
    public Resource resolveResource(
        HttpServletRequest request,
        String requestPath,
        List<? extends Resource> locations,
        ResourceResolverChain chain) {

      return resolve(requestPath, locations);
    }

    @Override
    public String resolveUrlPath(
        String resourcePath, List<? extends Resource> locations, ResourceResolverChain chain) {

      Resource resolvedResource = resolve(resourcePath, locations);
      if (resolvedResource == null) {
        return null;
      }
      try {
        return resolvedResource.getURL().toString();
      } catch (IOException e) {
        return resolvedResource.getFilename();
      }
    }

    private Resource resolve(String requestPath, List<? extends Resource> locations) {
      if (isIgnored(requestPath)) {
        return null;
      }
      if (isHandled(requestPath)) {
        return locations.stream()
            .map(loc -> createRelative(loc, requestPath))
            .filter(resource -> resource != null && resource.exists())
            .findFirst()
            .orElse(null);
      }
      return index;
    }

    private Resource createRelative(Resource resource, String relativePath) {
      try {
        return resource.createRelative(relativePath);
      } catch (IOException e) {
        return null;
      }
    }

    private boolean isIgnored(String path) {
      return ignoredPaths.stream()
        .map(path::startsWith).reduce((one, more) -> one || more).orElse(false);
    }

    private boolean isHandled(String path) {
      String extension = StringUtils.getFilenameExtension(path);
      return handledExtensions.stream().anyMatch(ext -> ext.equals(extension));
    }
  }
}
