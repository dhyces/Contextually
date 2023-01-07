package dhyces.contextually.data;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.LanguageProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Map;

public abstract class ContextProvider implements DataProvider {

    @Nullable LanguageProvider languageProvider;
    DataGenerator.PathProvider pathProvider;
    ExistingFileHelper fileHelper;
    String modid;
    final String contextPath = "contexts";
    final Map<ResourceLocation, IGeneratedContext> data = Maps.newHashMap();

    public ContextProvider(@Nullable LanguageProvider languageProvider, @NotNull DataGenerator generator, @NotNull ExistingFileHelper fileHelper, @NotNull String modid, @Nullable String folder) {
        this.languageProvider = languageProvider;
        Preconditions.checkNotNull(generator);
        this.pathProvider = generator.createPathProvider(DataGenerator.Target.RESOURCE_PACK, folder == null ? contextPath : contextPath + '/' + folder);
        Preconditions.checkNotNull(fileHelper);
        this.fileHelper = fileHelper;
        Preconditions.checkNotNull(modid);
        this.modid = modid;
    }

    protected abstract void addContexts();

    @Override
    public void run(CachedOutput cache) throws IOException {
        addContexts();
    }

    @Override
    public String getName() {
        return "Contextually Context Provider";
    }
}
