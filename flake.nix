{
  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";

    sbt = {
      url = "github:zaninime/sbt-derivation/master";
      inputs.nixpkgs.follows = "nixpkgs";
    };
  };

  outputs =
    {
      self,
      nixpkgs,
      sbt,
    }:
    let
      system = "x86_64-linux";
      pkgs = nixpkgs.legacyPackages.${system};
    in
    {
      packages.${system}.default =
        (sbt.mkSbtDerivation.${system}).withOverrides ({ stdenv = pkgs.llvmPackages.stdenv; })
          {
            pname = "scli";
            version = "0.1.0";
            src = ./.;
            depsSha256 = "sha256-LJS2FFpIJKLZM2CPQQ+bbaxK0H/HR08ZdV6D3kqb2q4=";
            env.NIX_CFLAGS_COMPILE = "-Wno-unused-command-line-argument";
            hardeningDisable = [ "fortify" ];
            buildPhase = ''
              sbt compile
            '';
            installPhase = ''
              sbt 'show stage'
              mkdir -p $out/bin
              cp target/scli $out/bin/
            '';
            nativeBuildInputs = [
              pkgs.which
            ];
            buildInputs = [
              pkgs.s2n-tls
            ];
          };

      devShells.${system}.myshell =
        with pkgs;
        mkShellNoCC {
          packages = [
            # -
            openjdk
          ];
        };
    };
}
