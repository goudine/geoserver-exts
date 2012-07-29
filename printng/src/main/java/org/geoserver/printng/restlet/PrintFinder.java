package org.geoserver.printng.restlet;

import org.geoserver.printng.api.PrintngReaderFactory;
import org.geoserver.rest.format.MediaTypes;
import org.restlet.Finder;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.Resource;

/**
 * 
 * @author Ian Schneider <ischneider@opengeo.org>
 */
public class PrintFinder extends Finder {

    private final PrintngReaderFactory readerFactory;

    public PrintFinder() {
        this(null);
    }

    // gets set from spring
    public PrintFinder(PrintngReaderFactory readerFactory) {
        this.readerFactory = readerFactory;
        MediaTypes.registerExtension("pdf", MediaType.APPLICATION_PDF);
        MediaTypes.registerExtension("jpg", MediaType.IMAGE_JPEG);
        MediaTypes.registerExtension("png", MediaType.IMAGE_PNG);
        MediaTypes.registerExtension("gif", MediaType.IMAGE_GIF);
    }

    @Override
    public Resource findTarget(Request request, Response response) {
        PrintngFacade facade = new PrintngFacade(request, response, readerFactory);
        return facade.getResource();
    }

}
