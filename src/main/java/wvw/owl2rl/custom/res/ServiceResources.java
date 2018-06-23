/**
* Copyright 2016 William Van Woensel

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
* 
* 
* @author wvw
* 
*/

package wvw.owl2rl.custom.res;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletContext;

import wvw.owl2rl.custom.config.Config;
import wvw.utils.IOUtils;
import wvw.utils.file.ManagedFile;
import wvw.utils.file.ManagedFiles;

public class ServiceResources {

	private ServletContext ctx;
	private String resPath = "/WEB-INF/";

	private ManagedFiles files = new ManagedFiles(1 * 1024 * 1024);

	public ServiceResources(ServletContext ctx) {
		this.ctx = ctx;
	}

	public ServletContext getCtx() {
		return ctx;
	}

	public String getResPath() {
		return resPath;
	}

	public InputStream getInputStream(String path) {
		// System.out.println(ctx.getResourcePaths("/WEB-INF/"));

		return ctx.getResourceAsStream(resPath + path);
	}

	public String getContents(String path) throws IOException {			
		return IOUtils.readFromStream(getInputStream(path));
	}

	protected ManagedFile getFile(String name) {
		File file = new File(name);

		System.out.println("?? " + name);
		if (!file.isAbsolute()) {
			File folder = new File(Config.get("storagePath"));

			file = new File(folder, name);
		}

		return files.getFile(file.getPath());
	}
}
